package com.cdek.courier.ui.base.gallery

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ContentResolver
import android.content.Intent
import android.content.res.Configuration
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdek.courier.App
import com.cdek.courier.BuildConfig
import com.cdek.courier.R
import com.cdek.courier.data.models.entities.FileEntity
import com.cdek.courier.databinding.FragmentGalleryBinding
import com.cdek.courier.utils.FileUtils
import com.cdek.courier.utils.REQUEST_TAKE_PHOTO
import com.cdek.courier.utils.TASK_NUMBER
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import java.io.File
import java.io.IOException
import javax.inject.Inject

class GalleryFragment : DaggerFragment(), GalleryAdapter.OnItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var rvAdapter: GalleryAdapter

    private val viewModel by lazy {
        ViewModelProviders.of(this@GalleryFragment, viewModelFactory)[GalleryViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGalleryBinding = DataBindingUtil
            .inflate(LayoutInflater.from(context), R.layout.fragment_gallery, container, false)
        binding.viewModel = viewModel

        arguments?.getString(TASK_NUMBER)?.let {
            viewModel.fetchTask(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        initObserve()
    }


    private fun initRecyclerView() {
        val llm =
            if (activity!!.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            } else {
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            }
        rv_photo_bank.layoutManager = llm
        rv_photo_bank.setHasFixedSize(true)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_TAKE_PHOTO -> {
                    checkCameraDuplicate()
                    viewModel.saveFileInfo(viewModel.imagePath)
                }
            }
        }
    }

    private fun startCamera() {
        val filePath = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = filePath?.let { FileUtils.newPhotoFile(it) }
        if (file != null) {
            viewModel.imagePath = filePath.toString() + "/" + file.name
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(activity!!.packageManager) != null) {
                val photoURI: Uri = FileProvider.getUriForFile(
                    App.instance,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                    intent.clipData = ClipData.newRawUri("", photoURI)
                    intent.addFlags(
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
                startActivityForResult(intent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    @SuppressLint("InlinedApi", "Recycle")
    private fun checkCameraDuplicate() {
        val proj = arrayOf(
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns._ID
        )
        val selection = MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME + " = ? "
        val selectionArgs = arrayOf("Camera")
        val cursor: Cursor? = App.instance.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, selection, selectionArgs,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " desc"
        )
        if (cursor != null) {
            val idxPath = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            if (cursor.count > 0 && idxPath > -1 && cursor.moveToFirst()) {
                val original = File(viewModel.imagePath)
                val cameraDupe = File(cursor.getString(idxPath))
                if (original.exists() && cameraDupe.exists()) { //if (original.length() == cameraDupe.length() && original.lastModified() == cameraDupe.lastModified()) {
                    if (original.length() == cameraDupe.length()) {
                        deleteFileFromMediaStore(App.instance.contentResolver, cameraDupe)
                        cameraDupe.delete()
                    }
                }
            }
            cursor.close()
        }
        val result = FileUtils.resizeAndSaveFile(File(viewModel.imagePath))
        if (result.isNotEmpty()) {
            showMessage(result)
        }
    }

    private fun deleteFileFromMediaStore(contentResolver: ContentResolver, file: File) {
        val canonicalPath: String = try {
            file.canonicalPath
        } catch (e: IOException) {
            file.absolutePath
        }
        val uri = MediaStore.Files.getContentUri("external")
        val result = contentResolver.delete(
            uri,
            MediaStore.Files.FileColumns.DATA + "=?", arrayOf(canonicalPath)
        )
        if (result == 0) {
            val absolutePath = file.absolutePath
            if (absolutePath != canonicalPath) {
                contentResolver.delete(
                    uri,
                    MediaStore.Files.FileColumns.DATA + "=?", arrayOf(absolutePath)
                )
            }
        }
    }

    override fun onItemClick(item: FileEntity) {
        viewModel.selectedItem.set(item)
    }

    private fun initObserve() {
        viewModel.photoList().observe(viewLifecycleOwner, Observer<List<FileEntity>> {
            it?.let {
                if (::rvAdapter.isInitialized) {
                    rvAdapter.setData(it)
                } else {
                    rvAdapter = GalleryAdapter(it, this@GalleryFragment)
                    rv_photo_bank.adapter = rvAdapter
                }
            }
        })

        viewModel.navigateToCamera.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.navigateToCameraHandled()
                startCamera()
            }
        })

        viewModel.navigateToTask.observe(viewLifecycleOwner, Observer {
            if (it) {
                viewModel.navigateToTaskHandled()
                findNavController().navigateUp()
            }
        })

        viewModel.message.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                showMessage(it)
                viewModel.messageHandled()
            }
        })
    }

    private fun showMessage(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearFindViewByIdCache()
    }
}