package com.hfad.buddha

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.buddha.databinding.FragmentQuoteBinding
import com.hfad.buddha.viewmodels.QuoteViewModel
import com.hfad.buddha.viewmodels.QuoteViewModelFactory
import kotlinx.coroutines.launch
import java.io.File

class QuoteFragment : Fragment() {

    private val viewModel: QuoteViewModel by viewModels {
        QuoteViewModelFactory(
            (activity?.application as BuddhaApplication).database.quotesDao(),
            (activity?.application as BuddhaApplication).database.settingsDao()
        )
    }

    private var _binding: FragmentQuoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuoteBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val path = context?.filesDir
        val file = File(path, "quotes_file.txt")
        Log.d("fille","$path")


        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val quoteAdapter = QuoteAdapter({
            val intent = Intent(Intent(context, EditQuoteActivity::class.java))
            intent.putExtra("quote", it)
            val splitQuotes = file.readText().split("\n")
            val id = splitQuotes.indexOf(it)
            intent.putExtra("id", id)
            startActivity(intent)
        }, {
                it ->
            val builder: AlertDialog.Builder? = context?.let { AlertDialog.Builder(it) }
            if(it.contains(" ")) {
                val str = it.split(" ")
                builder?.setTitle("Delete quote ${str[0]} ${str[1]}...?")
            } else
                builder?.setTitle("Delete quote $it?")
            builder?.setPositiveButton("OK") { _, _ ->

                val splitQuotes = file.readText().split("\n")
                val id = splitQuotes.indexOf(it)
                file.delete()
                file.createNewFile()
                for (i in splitQuotes.indices){
                    if (i != id)
                        if (id != splitQuotes.size-1)
                            if (i != splitQuotes.size - 1)
                                file.appendText("${splitQuotes[i]}\n")
                            else
                                file.appendText(splitQuotes[i])
                        else
                            if (i != splitQuotes.size - 2)
                                file.appendText("${splitQuotes[i]}\n")
                            else
                                file.appendText(splitQuotes[i])

                }
                //viewModel.deleteQuote(it)
                Log.d("fille","${splitQuotes.size}")
            }
            builder?.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            builder?.show()
        }, viewModel
        )

        recyclerView.adapter = quoteAdapter
//        lifecycle.coroutineScope.launch {
//            viewModel.allQuotes().collect {
//                quoteAdapter.submitList(it)
//            }
//        }
        val splitQuotes = file.readText().split("\n")
        quoteAdapter.submitList(splitQuotes)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}