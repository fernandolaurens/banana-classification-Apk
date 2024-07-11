//package com.laurens.klasifikasibanana.view.home
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import com.laurens.klasifikasibanana.R
//
//class DetailHomeFragment : Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_detail_home, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val name = arguments?.getString("EXTRA_NAME")
//        val description = arguments?.getString("EXTRA_DESCRIPTION")
//        val photoId = arguments?.getInt("EXTRA_PHOTO", 0)
//
//        val detailTitle: TextView = view.findViewById(R.id.tvHeroName)
//        val detailDesc: TextView = view.findViewById(R.id.desc)
//        val detailImage: ImageView = view.findViewById(R.id.imageDETAIL)
//
//        detailTitle.text = name
//        detailDesc.text = description
//        photoId?.let { detailImage.setImageResource(it) }
//    }
//
//    companion object {
//        @JvmStatic
//        fun newInstance(name: String, description: String, photoId: Int) =
//            DetailHomeFragment().apply {
//                arguments = Bundle().apply {
//                    putString("EXTRA_NAME", name)
//                    putString("EXTRA_DESCRIPTION", description)
//                    putInt("EXTRA_PHOTO", photoId)
//                }
//            }
//    }
//}