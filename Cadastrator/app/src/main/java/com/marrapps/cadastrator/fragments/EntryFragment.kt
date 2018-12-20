package com.marrapps.cadastrator.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.marrapps.cadastrator.R
import com.marrapps.cadastrator.database.EntryDatabase
import com.marrapps.cadastrator.interfaces.ServiceListener
import com.marrapps.cadastrator.model.Cep
import com.marrapps.cadastrator.model.Entry
import com.marrapps.cadastrator.model.dao.CepDAO
import kotlinx.android.synthetic.main.fragment_entry.view.*


class EntryFragment : Fragment(), ServiceListener {

    private lateinit var btnCheckCep: Button
    private lateinit var btnGo: Button
    private lateinit var btnMap: Button
    private lateinit var txtAddress: TextView
    private lateinit var txtNeighbourhood: TextView
    private lateinit var txtCity: TextView
    private lateinit var txtState: TextView
    private lateinit var txtCep: TextInputEditText
    private lateinit var txtName: TextInputEditText
    private lateinit var txtCpf: TextInputEditText
    private lateinit var txtDay: TextInputEditText
    private lateinit var txtMonth: TextInputEditText
    private lateinit var txtYear: TextInputEditText
    private lateinit var txtNumber: TextInputEditText
    private lateinit var txtMore: TextInputEditText
    private var isCepVerified = false

    private var entryDatabase: EntryDatabase? = null

    private lateinit var onGoClickListener: OnGoClickListener


    private val cepDAO = CepDAO()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_entry, container, false)

        val aContext: Context = this.context ?: requireContext()

        initViews(view)

        entryDatabase = EntryDatabase.getInstance(aContext)

        btnCheckCep.setOnClickListener {

            isCepVerified = false

            if (txtCep.text == null || txtCep.text.toString().length != 8) {

                Toast.makeText(context, "CEP inválido", Toast.LENGTH_SHORT).show()

                txtAddress.text = "Endereço"
                txtNeighbourhood.text = "Bairro"
                txtCity.text = "Cidade"
                txtState.text = "Estado"

            } else {

                cepDAO.getCep(aContext, txtCep.text.toString(), this)
            }
        }

        btnGo.setOnClickListener {

            if (isFillingCorrect()) {

                val entry = Entry()

                fillEntry(entry)

                val task = Runnable {
                    entryDatabase?.entryDao()?.insert(entry)
                }
                Thread(task).start()

                txtName.setText("")
                txtCpf.setText("")
                txtDay.setText("")
                txtMonth.setText("")
                txtYear.setText("")
                txtCep.setText("")
                txtNumber.setText("")
                txtMore.setText("")

                txtAddress.text = "Endereço"
                txtNeighbourhood.text = "Bairro"
                txtCity.text = "Cidade"
                txtState.text = "Estado"

                isCepVerified = false

                Toast.makeText(context, "Cadastro salvo", Toast.LENGTH_SHORT).show()

                onGoClickListener.onGoClick()

            } else {
                Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            }
        }

        btnMap.setOnClickListener {

            if (isCepVerified && !txtNumber.text.isNullOrBlank()) {

                val gmmIntentUri = Uri.parse("geo:0,0?q=" + txtNumber.text + txtAddress.text + txtCity.text)
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)

            } else {
                Toast.makeText(
                    context,
                    "Para consultar o mapa, primeiro verifique o CEP e preencha o número",
                    Toast.LENGTH_LONG
                ).show()

            }
        }

        return view
    }

    private fun fillEntry(entry: Entry) {
        val birth = StringBuilder()
        birth.append(txtDay.text).append("/").append(txtMonth.text).append("/").append(txtYear.text)

        entry.name = txtName.text.toString()
        entry.cpf = txtCpf.text.toString()
        entry.birth = birth.toString()
        entry.cep = txtCep.text.toString()
        entry.address = txtAddress.text.toString()
        entry.neighbourhood = txtNeighbourhood.text.toString()
        entry.city = txtCity.text.toString()
        entry.state = txtState.text.toString()
        entry.number = txtNumber.text.toString()
        if (!txtMore.text.isNullOrBlank()) {
            entry.compl = txtMore.text.toString()
        }
    }

    override fun onSucess(obtained: Any) {
        val cep: Cep = obtained as Cep

        if (cep.error) {
            Toast.makeText(context, "CEP inválido", Toast.LENGTH_SHORT).show()

            txtAddress.text = "Endereço"
            txtNeighbourhood.text = "Bairro"
            txtCity.text = "Cidade"
            txtState.text = "Estado"

        } else {
            txtAddress.text = cep.address
            txtNeighbourhood.text = cep.neighbourhood
            txtCity.text = cep.city
            txtState.text = cep.state

            isCepVerified = true
        }
    }

    override fun onError(throwable: Throwable) {

        Log.e("CEP REQUEST", throwable.message)
        Toast.makeText(context, "CEP inválido", Toast.LENGTH_SHORT).show()

        txtAddress.text = "Endereço"
        txtNeighbourhood.text = "Bairro"
        txtCity.text = "Cidade"
        txtState.text = "Estado"
    }

    private fun initViews(view: View) {
        btnCheckCep = view.btnEditCep
        txtCep = view.txtImputCep
        txtAddress = view.txtEditAddress
        txtNeighbourhood = view.txtEditNeighbourhood
        txtCity = view.txtEditCity
        txtState = view.txtEditState
        txtName = view.txtImputName
        txtCpf = view.txtImputCpf
        txtDay = view.txtImputDay
        txtMonth = view.txtImputMonth
        txtYear = view.txtImputYear
        txtNumber = view.txtImputNumber
        txtMore = view.txtImputMore
        btnGo = view.btnGo
        btnMap = view.btnEditMap
    }

    private fun isFillingCorrect(): Boolean {

        if (!(txtName.text.isNullOrBlank()) &&
            txtCpf.text != null &&
            txtCpf.text.toString().length == 11 &&
            txtDay.text != null &&
            txtDay.text.toString().length == 2 &&
            txtMonth.text != null &&
            txtMonth.text.toString().length == 2 &&
            txtYear.text != null &&
            txtYear.text.toString().length == 4 &&
            txtCep.text != null &&
            txtCep.text.toString().length == 8 &&
            txtNumber.text != null &&
            txtNumber.text.toString().isNotBlank() &&
            isCepVerified
        ) {
            return true
        }
        return false
    }

    interface OnGoClickListener {

        fun onGoClick()
    }

    fun setOnGoClickListener(activity: Activity) {
        onGoClickListener = activity as OnGoClickListener
    }


}
