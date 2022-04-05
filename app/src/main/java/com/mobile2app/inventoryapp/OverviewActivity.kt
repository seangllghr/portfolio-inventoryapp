package com.mobile2app.inventoryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.snackbar.Snackbar
import com.mobile2app.inventoryapp.databinding.ActivityOverviewBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class OverviewActivity : AppCompatActivity() {

    private val REQUEST_ADD_ITEM = 0;
    private lateinit var mInventoryBinding: ActivityOverviewBinding
    private lateinit var mInventoryViewModel: InventoryViewModel
    private lateinit var mBottomAppBar: BottomAppBar
    private lateinit var mInventoryAdapter: InventoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mInventoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_overview)
        mInventoryViewModel =
                InventoryViewModelFactory((application as InventoryApplication).inventoryRepository)
                        .create(InventoryViewModel::class.java)
        mInventoryBinding.inventoryViewModel = mInventoryViewModel
        mInventoryBinding.lifecycleOwner = this

        mBottomAppBar = mInventoryBinding.bottomAppBarOverview

        // Dealing with menus and navigation using pure MVVM is nightmarishly complex, and I'm
        // rapidly running out of airspeed and altitude. So we'll use a hybrid approach
        mBottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuItem_default_signOut -> {
                    finish()
                    true
                }
                R.id.menuItem_default_alerts -> TODO("Request notification permission")
                else -> false
            }
        }

        mInventoryBinding.fabOverview.setOnClickListener {
/*          TODO: For reasons that I can't even begin to grasp, the add item activity is broken.
            Instead, we are just calling a dummy "add item" to put data into the backend. The
            commented code *would* launch the add item activity if it didn't throw arcane XML errors.

            val intent = Intent(this, AddItemActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_ITEM)
*/
            GlobalScope.launch {
                (applicationContext as InventoryApplication).inventoryRepository
                    .addItem(InventoryItem(
                        item = "Demo Item",
                        category = "Demo Items",
                        quantity = 5,
                        description = "Dummy data for populating the database"
                    ))
            }
        }

        mInventoryViewModel.inventory.observe(this) {
            val recyclerView = mInventoryBinding.recyclerViewOverviewInventory
            mInventoryAdapter = InventoryAdapter(it)
            val layoutManager = GridLayoutManager(this, 2)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = mInventoryAdapter
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_ITEM) {
            when (resultCode) {
                RESULT_OK -> Snackbar.make(
                    mInventoryBinding.root,
                    R.string.additem_success,
                    Snackbar.LENGTH_SHORT
                )
            }
        }
    }

}