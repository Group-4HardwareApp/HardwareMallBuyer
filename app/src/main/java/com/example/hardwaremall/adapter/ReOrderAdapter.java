package com.example.hardwaremall.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwaremall.api.ProductService;
import com.example.hardwaremall.bean.OrderItems;
import com.example.hardwaremall.bean.Product;
import com.example.hardwaremall.utility.InternetConnectivity;
import com.example.hardwaremall.R;
import com.example.hardwaremall.databinding.CartItemListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReOrderAdapter extends RecyclerView.Adapter<ReOrderAdapter.ReOrderViewHolder> {
    private OnRecyclerViewClick listener;
    Context context;
    ArrayList<OrderItems> cartList;
    ArrayList<String> productId;
    InternetConnectivity connectivity = new InternetConnectivity();
    ProgressDialog pd;
    TextView tvAmt;
    int qtyInStock;
    Product p;
    double price, total, discount, offerprice;

    public ReOrderAdapter(Context context, ArrayList<OrderItems> cartList, TextView tvAmt) {
        this.context = context;
        this.cartList = cartList;
        this.tvAmt = tvAmt;
        //this.qtyInStock = qtyInStock;
        //this.offerprice = offerPrice;
    }

    @NonNull
    @Override
    public ReOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemListBinding binding = CartItemListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ReOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReOrderViewHolder holder, final int position) {
        final OrderItems cart = cartList.get(position);
        //final double qtyInStock = cart.getQtyInStock();
        int qty = 1;
        holder.binding.tvQty.setText("" + qty);
        cart.setQty(qty);

        ProductService.ProductApi api = ProductService.getProductApiInstance();
        Call<Product> call = api.viewProduct(cart.getProductId());
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.code() == 200){
                    p = response.body();
                    qtyInStock = p.getQtyInStock();
                    holder.binding.tvProductQty.setText("In stock : " + qtyInStock);
                    holder.binding.tvProductName.setText("" + cart.getName());
                    price = p.getPrice();
                    discount = p.getDiscount();
                    double dis = price * (discount / 100);
                    offerprice = price - dis;
                    holder.binding.tvProductPrice.setText("₹ " + offerprice);
                    //Log.e("adapter offer price", "==->" + offerprice);
                    //total = total + offerprice;
                    //Log.e("adapter product Price", "==->" + price);
                    //Log.e("adapter product Total", "==->" + total);

                    holder.binding.ivAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            /*price = p.getPrice();
                            discount = p.getDiscount();
                            double dis = price * (discount / 100);
                            offerprice = price - dis;
                            Log.e("adapter offer price", "==->" + offerprice);
                            //total = total + offerprice;
                            Log.e("adapter product Price", "==->" + price);
                            */
                            holder.binding.tvQty.getText().toString();
                            int q = Integer.parseInt(holder.binding.tvQty.getText().toString());
                            if (q < qtyInStock) {
                                q++;
                                holder.binding.tvQty.setText("" + q);
                                cart.setQty(q);
                                total = Double.parseDouble(tvAmt.getText().toString());
                                total = total + (offerprice);
                                tvAmt.setText("" + total);
                                cart.setTotal(q*offerprice);
                            }
                        }
                    });

                    holder.binding.ivSubrtact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Toast.makeText(context, "After click", Toast.LENGTH_SHORT).show();
                            //price = cart.getPrice();
                            price = p.getPrice();
                            discount = p.getDiscount();
                            double dis = price * (discount / 100);
                            offerprice = price - dis;

                            holder.binding.tvQty.getText().toString();
                            int q = Integer.parseInt(holder.binding.tvQty.getText().toString());
                            if (q > 1) {
                                q--;
                                holder.binding.tvQty.setText("" + q);
                                cart.setQty(q);
                                double total = Double.parseDouble(tvAmt.getText().toString());
                                total = total - (offerprice);
                                tvAmt.setText("" + total);
                                cart.setTotal(q*offerprice);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {

            }
        });

        Picasso.get().load(cart.getImageUrl()).placeholder(R.drawable.default_photo_icon).into(holder.binding.productImage);

        /*holder.binding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = p.getPrice();
                discount = p.getDiscount();
                double dis = price * (discount / 100);
                offerprice = price - dis;
                Log.e("adapter offer price", "==->" + offerprice);
                //total = total + offerprice;
                Log.e("adapter product Price", "==->" + price);

                holder.binding.tvQty.getText().toString();
                int q = Integer.parseInt(holder.binding.tvQty.getText().toString());
                if (q < qtyInStock) {
                    q++;
                    holder.binding.tvQty.setText("" + q);
                    cart.setQty(q);
                    total = Double.parseDouble(tvAmt.getText().toString());
                    total = total + (offerprice);
                    tvAmt.setText("" + total);
                    cart.setTotal(q*offerprice);
                }
            }
        });

        holder.binding.ivSubrtact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(context, "After click", Toast.LENGTH_SHORT).show();
                //price = cart.getPrice();
                price = p.getPrice();
                discount = p.getDiscount();
                double dis = price * (discount / 100);
                offerprice = price - dis;

                holder.binding.tvQty.getText().toString();
                int q = Integer.parseInt(holder.binding.tvQty.getText().toString());
                if (q > 1) {
                    q--;
                    holder.binding.tvQty.setText("" + q);
                    cart.setQty(q);
                    double total = Double.parseDouble(tvAmt.getText().toString());
                    total = total - (offerprice);
                    tvAmt.setText("" + total);
                    cart.setTotal(q*offerprice);
                }
            }
        });*/
        /*
        holder.binding.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectivity.isConnectedToInternet(context)) {
                    final AlertDialog ab = new AlertDialog.Builder(context).create();
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.alert_dialog, null);
                    ab.setView(view);

                    TextView tvtitleMsg = view.findViewById(R.id.tvTilteMsg);
                    tvtitleMsg.setText("You want to remove this product from cart");
                    CardView btnCancel = view.findViewById(R.id.btnCancel);
                    CardView btnOkay = view.findViewById(R.id.btnOkay);

                    btnOkay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pd = new ProgressDialog(context);
                            pd.setTitle("Removing");
                            pd.setMessage("Please wait...");
                            pd.show();
                            CartService.CartApi cartApi = CartService.getCartApiInstance();
                            Call<Cart> call = cartApi.removeProductFormCart(cart.getCartId());
                            call.enqueue(new Callback<Cart>() {
                                @Override
                                public void onResponse(Call<Cart> call, Response<Cart> response) {
                                    Log.e("data", "========>" + response);
                                    if (response.code() == 200) {
                                        Cart c = response.body();
                                        cartList.remove(position);
                                        pd.dismiss();
                                        notifyDataSetChanged();
                                        ab.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Cart> call, Throwable t) {
                                    Log.e("failed", "=========>" + t);
                                }
                            });
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ab.dismiss();
                        }
                    });
                    ab.show();
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ReOrderViewHolder extends RecyclerView.ViewHolder {
        CartItemListBinding binding;

        public ReOrderViewHolder(CartItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    OrderItems c = cartList.get(position);
                    if (position != RecyclerView.NO_POSITION && listener != null)
                        listener.onItemClick(c, position);
                }
            });
        }
    }

    public interface OnRecyclerViewClick {
        void onItemClick(OrderItems cart, int position);
    }

    public void setOnItemClicklistner(OnRecyclerViewClick listener) {
        this.listener = listener;
    }
}
