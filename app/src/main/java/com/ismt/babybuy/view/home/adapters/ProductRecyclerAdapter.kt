package com.ismt.babybuy.view.home.adapters

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ismt.babybuy.R
import com.ismt.babybuy.room.Product

import java.io.IOException

class ProductRecyclerAdapter(
    private val products: List<Product>,
    private val listener: ProductAdapterListener,
    private val context: Context
): RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var itemRootLayout: ConstraintLayout
        var itemImage: ImageView
        var itemTitle: TextView
        var itemPrice: TextView
        var itemDescription: TextView
        var itemDate : TextView

        init {
            itemRootLayout = view.findViewById(R.id.item_root_layout)
            itemImage = view.findViewById(R.id.iv_item_image)
            itemTitle = view.findViewById(R.id.tv_item_title)
            itemPrice = view.findViewById(R.id.tv_item_price)
            itemDate = view.findViewById(R.id.tv_item_date)
            itemDescription = view.findViewById(R.id.tv_item_description)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.product_list_tile, parent, false)

        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        holder.itemTitle.text = products[position].title
        holder.itemDescription.text = products[position].description
        holder.itemPrice.text = "$"+products[position].price
        holder.itemDate.text = products[position].date
        holder.itemRootLayout.setOnClickListener {
            listener.onItemClicked(products[position], position)
        }
        if (products[position].isPurchased == true) {
            val drawable = ContextCompat.getDrawable(context, R.drawable.ic_check_circle)
            holder.itemTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
        } else {
            holder.itemTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }

        holder.itemImage.post {
            var bitmap: Bitmap?
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                    context.contentResolver,
                    Uri.parse(products[position].image)
                )
//                bitmap = BitmapScalar.stretchToFill(
//                    bitmap,
//                    holder.itemImage.width,
//                    holder.itemImage.height
//                )
                holder.itemImage.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    interface ProductAdapterListener {
        fun onItemClicked(product: Product, position: Int)
    }
}