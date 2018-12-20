package com.marrapps.cadastrator.fragments


import android.app.Activity
import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_edit.view.*

fun newEditFragmentInstance(entry: Entry): EditFragment {

    val args = Bundle()
    args.putSerializable("entry", entry)

    val fragment = EditFragment()
    fragment.arguments = args
    return fragment
}

class EditFragment : Fragment(), ServiceListener {

    private lateinit var entry: Entry

    private lateinit var txtName: TextInputEditText
    private lateinit var btnCheckCep: Button
    private lateinit var btnGo: Button
    private lateinit var btnDelete: Button
    private lateinit var txtAddress: TextView
    private lateinit var txtNeighbourhood: TextView
    private lateinit var txtCity: TextView
    private lateinit var txtState: TextView
    private lateinit var txtCep: TextInputEditText
    private lateinit var txtCpf: TextInputEditText
    private lateinit var txtDay: TextInputEditText
    private lateinit var txtMonth: TextInputEditText
    private lateinit var txtYear: TextInputEditText
    private lateinit var txtNumber: TextInputEditText
    private lateinit var txtMore: TextInputEditText
    private var isCepVerified = false

    private val cepDAO = CepDAO()

    private var entryDatabase: EntryDatabase? = null

    private lateinit var onGoClickListener: OnGoClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        val aContext: Context = this.context ?: requireContext()

        entryDatabase = EntryDatabase.getInstance(aContext)

        initViews(view)

        entry = arguments?.getSerializable("entry") as Entry

        fillTexts()

        btnCheckCep.setOnClickListener{
            isCepVerified = true

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

            if (isFillingCorrect()){

                fillEntry()

                val task = Runnable {
                    entryDatabase?.entryDao()?.update(entry)
                }
                Thread(task).start()

                Toast.makeText(context, "Cadastro atualizado", Toast.LENGTH_SHORT).show()

                onGoClickListener.onGoBackClick()

            }else{
                Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener{

            val task = Runnable {
                entryDatabase?.entryDao()?.delete(entry)
            }
            Thread(task).start()

            Toast.makeText(context, "Cadastro deletado", Toast.LENGTH_SHORT).show()

            onGoClickListener.onGoBackClick()
        }

        return view
    }

    private fun initViews(view: View) {
        btnCheckCep = view.btnEditCep
        txtCep = view.txtEditCep
        txtAddress = view.txtEditAddress
        txtNeighbourhood = view.txtEditNeighbourhood
        txtCity = view.txtEditCity
        txtState = view.txtEditState
        txtName = view.txtEditName
        txtCpf = view.txtEditCpf
        txtDay = view.txtEditDay
        txtMonth = view.txtEditMonth
        txtYear = view.txtEditYear
        txtNumber = view.txtEditNumber
        txtMore = view.txtEditMore
        btnGo = view.btnEditSave
        btnDelete = view.btnEditDelete
    }

    private fun fillTexts() {
        txtName.setText(entry.name)
        txtCpf.setText(entry.cpf)
        txtDay.setText(entry.birth.substring(0..1))
        txtMonth.setText(entry.birth.substring(3..4))
        txtYear.setText(entry.birth.substring(6..9))
        txtCep.setText(entry.cep)
        txtAddress.text = entry.address
        txtNeighbourhood.text = entry.neighbourhood
        txtCity.text = entry.city
        txtState.text = entry.state
        txtNumber.setText(entry.number)
        txtMore.setText(entry.compl)

        isCepVerified = true
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

        isCepVerified = false
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
        ){
            return true
        }
        return false
    }

    private fun fillEntry() {
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

    interface OnGoClickListener{

        fun onGoBackClick()
    }

    fun setOnGoClickListener(activity: Activity) {
        onGoClickListener = activity as OnGoClickListener
    }
}
