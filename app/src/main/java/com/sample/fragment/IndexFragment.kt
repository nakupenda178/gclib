package com.sample.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.view.View
import com.github.guqt178.fragment.AppleFragment
import com.sample.test.R

class IndexFragment : AppleFragment(), View.OnClickListener {


    private var mToolbar: Toolbar? = null


    override fun initView(view: View, savedInstanceState: Bundle?) {
        mToolbar = view.findViewById(R.id.toolbar)
        view.findViewById<View>(R.id.btn_menu_more).setOnClickListener(this)
        view.findViewById<View>(R.id.btn_argument).setOnClickListener(this)
        view.findViewById<View>(R.id.btn_for_result).setOnClickListener(this)
        view.findViewById<View>(R.id.btn_stack).setOnClickListener(this)
    }

    override fun layoutId(): Int = R.layout.fragment_main


    override fun onClick(v: View) {
        val id = v.id
        when (id) {
            R.id.btn_menu_more -> {
                //startFragment(MoreMenuFragment::class.java)
            }
            R.id.btn_for_result -> {
                startFragmentForResult(ResultFragment::class.java, 100)
            }
            R.id.btn_argument -> {
                val bundle = Bundle()
                bundle.putString("hehe", "呵呵哒")
                bundle.putString("meng", "萌萌哒")
                bundle.putString("bang", "棒棒哒")
                bundle.putString("meme", "么么哒")

                // Create fragment_menu for bundle.
                val fragment: AppleFragment = fragment(MainFragment::class.java, bundle)
                startFragment(fragment)
            }
            R.id.btn_stack -> {

                // Second argument false: don't join the back stack.
                //startFragment(StackFragment::class.java, false)
            }
        }
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, result: Bundle?) {
        when (requestCode) {
            100 -> {
                if (resultCode == RESULT_OK) {
                    val message = result!!.getString("message")
                    AlertDialog.Builder(context)
                            .setCancelable(true)
                            .setTitle("结果")
                            .setMessage(message)
                            .setPositiveButton("ok") { dialog, which ->
                                dialog.dismiss()
                            }
                            .show()
                }
            }
        }
    }

}