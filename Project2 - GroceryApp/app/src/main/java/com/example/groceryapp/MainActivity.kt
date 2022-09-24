package com.example.groceryapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), GroceryRVAdapter.GroceryItemClickInterface {
    lateinit var itemsRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var list: List<GroceryItems>
    lateinit var groceryRVAdapter: GroceryRVAdapter
    lateinit var groceryViewModel: GroceryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemsRV = findViewById(R.id.idRVItems)
        addFAB = findViewById(R.id.idFABAdd)
        list = ArrayList<GroceryItems>()
        groceryRVAdapter = GroceryRVAdapter(list, this)
        itemsRV.layoutManager = LinearLayoutManager(this)
        itemsRV.adapter = groceryRVAdapter
        val groceryRepository = GroceryRepository(GroceryDatabase(this))
        val factory = GroceryViewModelFactory(groceryRepository)
        groceryViewModel = ViewModelProvider(this, factory).get(GroceryViewModel::class.java)
//        groceryViewModel.getAllGroceryItems().observe(this, {
//            groceryRVAdapter.list = it
//            groceryRVAdapter.notifyDataSetChanged()
//        })
        groceryViewModel.getAllGroceryItems().observe(this) {
            groceryRVAdapter.list = it
            groceryRVAdapter.notifyDataSetChanged()
        }

        addFAB.setOnClickListener{
            openDialog()
        }
    }

    fun openDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.grocery_add_dialog)

        val cancelBtn = dialog.findViewById<Button>(R.id.idBtnCancel)
        val addBtn = dialog.findViewById<Button>(R.id.idBtnAdd)
        val itemNameEt = dialog.findViewById<EditText>(R.id.idEditItemName)
        val itemQuantityEt = dialog.findViewById<EditText>(R.id.idEditItemQuantity)
        val itemPriceEt = dialog.findViewById<EditText>(R.id.idEditItemPrice)

        cancelBtn.setOnClickListener{
            dialog.dismiss()
        }

        addBtn.setOnClickListener{
            val itemName: String = itemNameEt.text.toString()
            val itemQuantity: String = itemQuantityEt.text.toString()
            val itemPrice: String = itemPriceEt.text.toString()
            val qt:Int = itemQuantity.toInt()
            val pr:Int = itemPrice.toInt()

            if(itemName.isNotEmpty() && itemQuantity.isNotEmpty() && itemPrice.isNotEmpty()){
                val item = GroceryItems(itemName, qt, pr)
                groceryViewModel.insert(item)
                Toast.makeText(applicationContext, "Item Inserted..", Toast.LENGTH_SHORT).show()
                groceryRVAdapter.notifyDataSetChanged()
                dialog.dismiss()
            } else {
                Toast.makeText(applicationContext, "Please Enter All Data", Toast.LENGTH_LONG).show()
            }
        }
        dialog.show()
    }

    override fun onItemClick(groceryItems: GroceryItems) {
        groceryViewModel.delete(groceryItems)
        groceryRVAdapter.notifyDataSetChanged()
        Toast.makeText(applicationContext, "Item Deleted..", Toast.LENGTH_SHORT).show()
    }
}