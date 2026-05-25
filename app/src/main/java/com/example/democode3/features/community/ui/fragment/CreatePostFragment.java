package com.example.democode3.features.community.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.democode3.R;
import com.example.democode3.features.community.api.request.CreatePostRequest;
import com.example.democode3.features.community.api.response.UploadImageResponse;
import com.example.democode3.features.community.repository.CommunityRepository;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePostFragment extends Fragment {

    private EditText edtContent;

    private EditText edtCode;

    private ImageView imgPreview;

    private MaterialButton btnAddImage;

    private MaterialButton btnRemoveImage;

    private MaterialButton btnPost;

    private CommunityRepository repository;

    private Uri selectedImageUri;

    // =====================================
    // IMAGE PICKER
    // =====================================

    private final ActivityResultLauncher<Intent>
            imagePickerLauncher =

            registerForActivityResult(

                    new ActivityResultContracts.StartActivityForResult(),

                    result -> {

                        if (

                                result.getResultCode() == Activity.RESULT_OK

                                        &&

                                        result.getData() != null
                        ) {

                            selectedImageUri =
                                    result.getData().getData();

                            imgPreview.setVisibility(
                                    View.VISIBLE
                            );

                            btnRemoveImage.setVisibility(
                                    View.VISIBLE
                            );

                            Glide.with(requireContext())

                                    .load(selectedImageUri)

                                    .into(imgPreview);
                        }
                    }
            );

    // =====================================
    // ON CREATE
    // =====================================

    @Nullable
    @Override
    public View onCreateView(

            @NonNull LayoutInflater inflater,

            @Nullable ViewGroup container,

            @Nullable Bundle savedInstanceState
    ) {

        View view =

                inflater.inflate(

                        R.layout.fragment_create_post,

                        container,

                        false
                );

        initViews(view);

        repository =
                new CommunityRepository(
                        requireContext()
                );

        setupClick();

        return view;
    }

    // =====================================
    // INIT
    // =====================================

    private void initViews(
            View view
    ) {

        edtContent =
                view.findViewById(
                        R.id.edtContent
                );

        edtCode =
                view.findViewById(
                        R.id.edtCode
                );

        imgPreview =
                view.findViewById(
                        R.id.imgPreview
                );

        btnAddImage =
                view.findViewById(
                        R.id.btnAddImage
                );

        btnRemoveImage =
                view.findViewById(
                        R.id.btnRemoveImage
                );

        btnPost =
                view.findViewById(
                        R.id.btnPost
                );
    }

    // =====================================
    // CLICK
    // =====================================

    private void setupClick() {

        // =========================
        // ADD IMAGE
        // =========================

        btnAddImage.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Intent.ACTION_PICK
                    );

            intent.setType("image/*");

            imagePickerLauncher.launch(intent);
        });

        // =========================
        // REMOVE IMAGE
        // =========================

        btnRemoveImage.setOnClickListener(v -> {

            selectedImageUri = null;

            imgPreview.setImageDrawable(null);

            imgPreview.setVisibility(
                    View.GONE
            );

            btnRemoveImage.setVisibility(
                    View.GONE
            );
        });

        // =========================
        // POST
        // =========================

        btnPost.setOnClickListener(v -> {

            submitPost();
        });
    }

    // =====================================
    // SUBMIT POST
    // =====================================

    private void submitPost() {

        String content =
                edtContent.getText()
                        .toString()
                        .trim();

        String code =
                edtCode.getText()
                        .toString()
                        .trim();

        if (

                TextUtils.isEmpty(content)

                        &&

                        TextUtils.isEmpty(code)
        ) {

            Toast.makeText(

                    requireContext(),

                    "Hãy nhập nội dung",

                    Toast.LENGTH_SHORT

            ).show();

            return;
        }

        btnPost.setEnabled(false);

        CreatePostRequest request =
                new CreatePostRequest();

        request.content = content;

        request.codeSnippet = code;

        // =========================
        // NO IMAGE
        // =========================

        if (selectedImageUri == null) {

            createPost(request);

            return;
        }

        // =========================
        // HAS IMAGE
        // =========================

        try {

            InputStream inputStream =

                    requireContext()

                            .getContentResolver()

                            .openInputStream(selectedImageUri);

            File tempFile =

                    File.createTempFile(

                            "upload",

                            ".jpg",

                            requireContext().getCacheDir()
                    );

            FileOutputStream outputStream =
                    new FileOutputStream(tempFile);

            byte[] buffer = new byte[4096];

            int read;

            while ((read = inputStream.read(buffer)) != -1) {

                outputStream.write(buffer, 0, read);
            }

            outputStream.flush();

            outputStream.close();

            inputStream.close();

            // =========================
            // FIX CHÍNH NẰM Ở ĐÂY
            // =========================

            RequestBody requestFile =

                    RequestBody.create(

                            MediaType.parse("image/*"),

                            tempFile
                    );

            MultipartBody.Part body =

                    MultipartBody.Part.createFormData(

                            "file",

                            tempFile.getName(),

                            requestFile
                    );

            // =====================
            // UPLOAD IMAGE
            // =====================

            repository.uploadImage(

                    body,

                    new Callback<UploadImageResponse>() {

                        @Override
                        public void onResponse(

                                Call<UploadImageResponse> call,

                                Response<UploadImageResponse> response
                        ) {

                            Log.d(
                                    "UPLOAD_RESPONSE",
                                    String.valueOf(response.body())
                            );

                            if (

                                    response.isSuccessful()

                                            &&

                                            response.body() != null
                            ) {

                                String imageUrl =
                                        response.body().result;

                                Log.d(
                                        "IMAGE_URL",
                                        imageUrl
                                );

                                Toast.makeText(

                                        requireContext(),

                                        imageUrl,

                                        Toast.LENGTH_LONG

                                ).show();

                                request.imageUrl = imageUrl;

                                createPost(request);

                            } else {

                                btnPost.setEnabled(true);

                                Toast.makeText(

                                        requireContext(),

                                        "Upload ảnh thất bại",

                                        Toast.LENGTH_SHORT

                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(

                                Call<UploadImageResponse> call,

                                Throwable t
                        ) {

                            btnPost.setEnabled(true);

                            t.printStackTrace();

                            Toast.makeText(

                                    requireContext(),

                                    t.getMessage(),

                                    Toast.LENGTH_LONG

                            ).show();
                        }
                    }
            );

        } catch (Exception e) {

            btnPost.setEnabled(true);

            e.printStackTrace();

            Toast.makeText(

                    requireContext(),

                    e.getMessage(),

                    Toast.LENGTH_LONG

            ).show();
        }
    }

    // =====================================
    // CREATE POST
    // =====================================

    private void createPost(
            CreatePostRequest request
    ) {

        repository.createPost(

                request,

                new Callback<Void>() {

                    @Override
                    public void onResponse(

                            Call<Void> call,

                            Response<Void> response
                    ) {

                        btnPost.setEnabled(true);

                        Toast.makeText(

                                requireContext(),

                                "Đăng bài thành công",

                                Toast.LENGTH_SHORT

                        ).show();

                        requireActivity()

                                .getSupportFragmentManager()

                                .popBackStack();
                    }

                    @Override
                    public void onFailure(

                            Call<Void> call,

                            Throwable t
                    ) {

                        btnPost.setEnabled(true);

                        t.printStackTrace();

                        Toast.makeText(

                                requireContext(),

                                t.getMessage(),

                                Toast.LENGTH_LONG

                        ).show();
                    }
                }
        );
    }
}