package com.example.pizzaapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzaapp.model.MenuModel

class AddMenuActivity : AppCompatActivity() {
    lateinit var image: ImageView
    companion object{
        val IMAGE_REQUEST_CODE = 100
    }
    //instance
    val image = findViewById(R.id.imageMenu)
    val textId : EditText = findViewById(R.id.menuId)
    val textName : EditText = findViewById(R.id.menuName)
    val textPrice : EditText = findViewById(R.id.menuPrice)
    val btnAddImage : Button = findViewById(R.id.buttonAddImage)
    val btnSaveMenu : Button = findViewById(R.id.buttonSaveMenu)

    //event saat buttom add (+) di-klik
    btnAddImage.setOnClickListener {
        pickImageGalery()
    }

    private fun pickImageGalery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK){
            image.setImageURI(data?.data)
        }
        //event saat button save diklik
        btnSaveMenu.setOnClickListener{
            //object class databaseHelper
            val databaseHelper = DatabaseHelper(this)

            val id : Int = textId.text.toString().toInt()
            val name : String = textName.text.toString().trim()
            val price : Int = textPrice.text.toString().toInt()
            val bitmapDrawable : BitmapDrawable = image.drawable as BitmapDrawable
            val bitmap : Bitmap = bitmapDrawable.bitmap

            val menuModel = MenuModel(id,name,price,bitmap)
            databaseHelper.addMenu(menuModel)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_menu)

    getSupportActionBar()?.hide()
    }

    override fun onCreateView(
        inflater : LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_makanan,container,false)
        val rvmakanan : RecyclerView = view.findViewById(R.id.recyclerMakanan)
        rvmakanan.layoutManager = LinearLayoutManager(activity)
        rvmakanan.adapter = MenuAdapter()

        val buttonAdd : Button = view.findViewById(R.id.buttonAddMenu)

        buttonAdd.setOnClickListener{
            requireActivity().run{
                startActivity(Intent(this, AddMenuActivity::class.java))
                finish()
            }
        }
        return view
    }
}