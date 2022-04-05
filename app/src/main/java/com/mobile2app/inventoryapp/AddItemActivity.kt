package com.mobile2app.inventoryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.mobile2app.inventoryapp.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private lateinit var mAddItemBinding: ActivityAddItemBinding
    private lateinit var mAddItemViewModel: AddItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAddItemBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_item)
        mAddItemViewModel =
            AddItemViewModelFactory((application as InventoryApplication).inventoryRepository)
                .create(AddItemViewModel::class.java)
        mAddItemBinding.addItemViewModel = mAddItemViewModel
        mAddItemBinding.lifecycleOwner = this

        mAddItemViewModel.newItemCreated.observe(this) {
            when (it) {
                AddItemViewModel.ItemAddResult.OK -> {
                    val intent = Intent()
                    setResult(RESULT_OK, intent)
                    finish()
                }
                AddItemViewModel.ItemAddResult.CANCELLED -> {
                    val intent = Intent()
                    setResult(RESULT_CANCELED, intent)
                    finish()
                }
                AddItemViewModel.ItemAddResult.FAILED -> {
                    Snackbar.make(
                        mAddItemBinding.root,
                        R.string.additem_failed,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                else -> {/* Do nothing */}
            }
        }
    }

}