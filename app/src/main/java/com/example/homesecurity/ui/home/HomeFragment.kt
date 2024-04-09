package com.example.homesecurity.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.homesecurity.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val peopleBoxList = mutableListOf<PeopleBox>(PeopleBox("matteo", "image/matteo", "il migliore"), PeopleBox("valentina", "image/valentina", "sorella"), PeopleBox("madre", "image/madre", "mia madre"), PeopleBox("padre", "image/padre", "mio padre"))

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val alarmButton: Button = binding.btnAlarm
        val alarmText: TextView = binding.tvAlarm

        // Prendo il RecyclerView dal layout
        val peopleRecyclerView: RecyclerView = binding.recyclerViewPeople
        peopleRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //creo un'istanza dell'adapter e ci collego il recyclerview
        val peopleAdapter = PeopleBoxAdapter(requireContext(), peopleBoxList)
        peopleRecyclerView.adapter = peopleAdapter;

        alarmButton.setOnClickListener {
            if(alarmButton.text == "ON") {
                alarmButton.text = "OFF";
                alarmText.text = "allarme acceso"
            }
            else{
                alarmButton.text = "ON";
                alarmText.text = "allarme spento"
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
