package com.baharudin.pengaduanapp.presentation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.baharudin.pengaduanapp.R
import com.baharudin.pengaduanapp.databinding.ActivityHomeBinding
import com.baharudin.pengaduanapp.domain.model.CategoryModel
import com.baharudin.pengaduanapp.presentation.adapter.CategoryAdapter
import com.baharudin.pengaduanapp.presentation.viewmodel.PostReportViewState
import com.baharudin.pengaduanapp.presentation.viewmodel.ReportViewModel
import com.baharudin.pengaduanapp.util.RealPathUtil
import com.baharudin.pengaduanapp.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private val viewModel : ReportViewModel by viewModels()
    private val PERMISSION_REQ_ID = 22
    private var dataList = ArrayList<CategoryModel>()
    var filename = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapterCategory()
        observe()
        setupListCategory()
        imagePicker()
        sendReport()
    }

    private fun setupAdapterCategory(){
        val categoryAdapter = CategoryAdapter(dataList)
        categoryAdapter.setItemClickListener(object : CategoryAdapter.OnItemClickListener{
            override fun onClick(filterModel: CategoryModel) {
                Toast.makeText(this@HomeActivity, "${filterModel.nama}", Toast.LENGTH_SHORT).show()
                binding.addCategory.text = filterModel.nama
            }
        })
        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
    }

    private fun observe() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { report ->
                handleState(report)
            }
            .launchIn(lifecycleScope)
    }

    private fun imagePicker() {
        binding.imvPhoto.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, 10)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
        }
    }

    private fun sendReport(){
        binding.btnSubmit.setOnClickListener {
            val nama = binding.addEdNamaPelapor.text.toString().trim()
            val alamat = binding.addEdNamaPelapor.text.toString().trim()
            val telp = binding.addEdNamaPelapor.text.toString().trim()
            val tersangka = binding.addEdNamaPelapor.text.toString().trim()
            val satuan = binding.addEdNamaPelapor.text.toString().trim()
            if (validate(nama, alamat, telp, tersangka, satuan)){
                var param : HashMap<String, RequestBody> = HashMap<String, RequestBody>()
                param.apply {
                    param["name"] = createRequestBody(binding.addEdNamaPelapor.text.toString())
                    param["address"] = createRequestBody(binding.addEdAlamatPelapor.text.toString())
                    param["phone"] = createRequestBody(binding.addEdTelpPelapor.text.toString())
                    param["reported"] = createRequestBody(binding.addEdNamaTerlapor.text.toString())
                    param["work_unit"] = createRequestBody(binding.addEdSatuanKerja.text.toString())
                    param["deed"] = createRequestBody(binding.addEdDescription.text.toString())
                    param["category"] = createRequestBody(binding.addCategory.text.toString())
                }
                val imageFile = File(filename)
                val requestBody =
                    RequestBody.create(
                        "multipart/form-file".toMediaTypeOrNull(),
                        imageFile
                    )
                val partFile1 =
                    MultipartBody.Part.createFormData(
                        "incident_photo",
                        imageFile.name,
                        requestBody
                    )
                viewModel.uploadReport(param, partFile1)
            }
        }
    }

    fun movePage(){
        val intent = Intent(this, SuccessActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    fun createRequestBody(s : String) : RequestBody {
        return RequestBody.create(
            MULTIPART_FORM_DATA.toMediaTypeOrNull(), s
        )
    }

    private fun validate(nama : String, alamat : String, telp : String, tersangka : String, satuan : String) : Boolean{
        resetAllError()
        if(nama.isEmpty()){
            setNameError("Isikan Nama")
            return false
        }
        if(alamat.isEmpty()){
            setAlamatError("Isikan Alamat")
            return false
        }
        if(telp.isEmpty()){
            setTelpError("Isikan Telepon")
            return false
        }
        if(tersangka.isEmpty()){
            setTersangkaError("Isikan Nama Terlapor")
            return false
        }
        if(satuan.isEmpty()){
            setSatuanError("Isikan Satuan Kerja")
            return false
        }
        return true
    }

    private fun setNameError(e : String?) {
        binding.textNamaPelapor.error = e
    }
    private fun setAlamatError(e : String?) {
        binding.textAlamatPelapor.error = e
    }
    private fun setTelpError(e : String?) {
        binding.textTelpPelapor.error = e
    }
    private fun setTersangkaError(e : String?) {
        binding.textNamaTerlapor.error = e
    }
    private fun setSatuanError(e : String?) {
        binding.textSatuanKerja.error = e
    }

    private fun handleState(state: PostReportViewState){
        when(state){
            is PostReportViewState.IsLoading -> handleLoading(state.isLoading)
            is PostReportViewState.SuccessCreate -> {
                movePage()
            }
            is PostReportViewState.ShowToast -> this.showToast(state.message)
            is PostReportViewState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading : Boolean){
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQ_ID) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 10 && resultCode === RESULT_OK) {
            val uri: Uri = data!!.data!!
            val context: Context = this
            filename = RealPathUtil.getRealPath(context, uri).toString()
            val bitmap = BitmapFactory.decodeFile(filename)
            binding.imvPhoto.setImageBitmap(bitmap)
        }

    }
    private fun setupListCategory(){
        dataList.add(
            CategoryModel(
                "perdata"
            )
        )
        dataList.add(
            CategoryModel(
                "tata usaha"
            )
        )
        dataList.add(
            CategoryModel(
                "intelejen"
            )
        )
        dataList.add(
            CategoryModel(
                "pidana khusus"
            )
        )
    }

    private fun resetAllError() {
        setAlamatError(null)
        setNameError(null)
        setTelpError(null)
        setTersangkaError(null)
        setSatuanError(null)
    }
    companion object {
        const val MULTIPART_FORM_DATA = "multipart/form-data"
    }
}