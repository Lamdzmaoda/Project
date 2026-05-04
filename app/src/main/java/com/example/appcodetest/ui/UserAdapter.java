package com.example.appcodetest.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcodetest.R;
import com.example.appcodetest.api.ApiService;
import com.example.appcodetest.api.RetrofitClient;
import com.example.appcodetest.model.UserResponse;
import com.example.appcodetest.utils.Prefs;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.VH> {

    Context context;
    List<UserResponse> list;
    ApiService api;

    public UserAdapter(Context context, List<UserResponse> list) {
        this.context = context;
        this.list = list;
        api = RetrofitClient.getClient().create(ApiService.class);
    }

    static class VH extends RecyclerView.ViewHolder {

        TextView txtName, txtId, txtRole, txtPermission;
        Button btnEdit, btnDelete;

        public VH(View v) {
            super(v);

            txtName = v.findViewById(R.id.txtName);
            txtId = v.findViewById(R.id.txtId);
            txtRole = v.findViewById(R.id.txtRole);
            txtPermission = v.findViewById(R.id.txtPermission);

            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {

        UserResponse user = list.get(i);

        h.txtName.setText(user.getFullName());
        h.txtId.setText("ID: " + user.id);

        if (user.roles != null && !user.roles.isEmpty()) {
            UserResponse.Role role = user.roles.iterator().next();
            h.txtRole.setText("Role: " + role.name);
            h.txtPermission.setText("");
        } else {
            h.txtRole.setText("Role: USER");
            h.txtPermission.setText("");
        }

        // =========================
        // ❌ DELETE REAL API
        // =========================
        h.btnDelete.setOnClickListener(v -> {

            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận")
                    .setMessage("Xóa user này?")
                    .setPositiveButton("Xóa", (d, w) -> {

                        String token = "Bearer " + Prefs.getToken(context);

                        api.deleteUser(token, user.id)
                                .enqueue(new Callback<Object>() {
                                    @Override
                                    public void onResponse(Call<Object> call, Response<Object> response) {

                                        if (response.isSuccessful()) {

                                            int pos = h.getAdapterPosition();
                                            if (pos != RecyclerView.NO_POSITION) {
                                                list.remove(pos);
                                                notifyItemRemoved(pos);
                                            }

                                            Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();

                                        } else {
                                            Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Object> call, Throwable t) {
                                        Toast.makeText(context, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        // =========================
        // ✏️ EDIT POPUP + API
        // =========================
        h.btnEdit.setOnClickListener(v -> {

            View dialogView = LayoutInflater.from(context)
                    .inflate(R.layout.dialog_edit_user, null);

            EditText edtFirst = dialogView.findViewById(R.id.edtFirstName);
            EditText edtLast = dialogView.findViewById(R.id.edtLastName);

            edtFirst.setText(user.firstName);
            edtLast.setText(user.lastName);

            new AlertDialog.Builder(context)
                    .setTitle("Sửa user")
                    .setView(dialogView)
                    .setPositiveButton("Lưu", (d, w) -> {

                        try {
                            JSONObject json = new JSONObject();
                            json.put("firstName", edtFirst.getText().toString());
                            json.put("lastName", edtLast.getText().toString());

                            RequestBody body = RequestBody.create(
                                    MediaType.parse("application/json"),
                                    json.toString()
                            );

                            String token = "Bearer " + Prefs.getToken(context);

                            api.updateUser(token, user.id, body)
                                    .enqueue(new Callback<Object>() {
                                        @Override
                                        public void onResponse(Call<Object> call, Response<Object> response) {

                                            if (response.isSuccessful()) {

                                                user.firstName = edtFirst.getText().toString();
                                                user.lastName = edtLast.getText().toString();

                                                notifyItemChanged(h.getAdapterPosition());

                                                Toast.makeText(context, "Đã cập nhật", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(context, "Update thất bại", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Object> call, Throwable t) {
                                            Toast.makeText(context, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}