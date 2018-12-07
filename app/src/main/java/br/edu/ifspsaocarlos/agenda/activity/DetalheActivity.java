package br.edu.ifspsaocarlos.agenda.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Calendar;

import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import br.edu.ifspsaocarlos.agenda.R;


public class DetalheActivity extends AppCompatActivity implements View.OnClickListener {

    private Contato c;
    private ContatoDAO cDAO;
    private EditText txtName;
    private EditText txtPhone;
    private EditText txtMail;
    private EditText txtDate;
    private ImageButton imageCaledar;
    private ImageButton imageAddPhone;
    private TextView textViewBirthday;
    private LinearLayout phoneLinearLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textViewBirthday = (TextView)findViewById(R.id.txtAnniversary);
        textViewBirthday.setText(getString(R.string.anniversary) + " " + getString(R.string.day_month));

        txtName = (EditText) findViewById(R.id.editName);
        txtPhone = (EditText) findViewById(R.id.editPhone);

        txtMail = (EditText) findViewById(R.id.editEmail);
        txtDate = (EditText) findViewById(R.id.editDate);

        imageCaledar = (ImageButton)findViewById(R.id.imgCalendar);
        imageCaledar.setOnClickListener(this);

        imageAddPhone = (ImageButton)findViewById(R.id.btAddPhone);
        imageAddPhone.setOnClickListener(this);

        phoneLinearLayout = findViewById(R.id.addPhones);

        if (getIntent().hasExtra("contato")) {
            this.c = (Contato) getIntent().getSerializableExtra("contato");

            txtName.setText(c.getNome());

            String multFones[] = c.getFone().split(";");

            if (multFones.length <= 1) txtPhone.setText(multFones[0]);
            else {
                txtPhone.setText(multFones[0]);
                addNewFone(multFones);
            }

            txtMail.setText(c.getEmail());
            txtDate.setText(c.getBirthday());

            int fav = c.getFavorite();

            Switch favoriteSwitch = (Switch) findViewById(R.id.switchFavorite);

            if(fav == 1) favoriteSwitch.setChecked(true);
            else favoriteSwitch.setChecked(false);

            int pos = c.getNome().indexOf(" ");
            if (pos == -1) pos = c.getNome().length();
            setTitle(c.getNome().substring(0, pos));

        }

        cDAO = new ContatoDAO(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhe, menu);
        if (!getIntent().hasExtra("contato")) {
            MenuItem item = menu.findItem(R.id.delContato);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salvarContato:
                salvar();
                return true;
            case R.id.delContato:
                apagar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void apagar() {
        cDAO.removeContact(c);

        Intent resultIntent = new Intent();
        setResult(3, resultIntent);
        finish();
    }

    private void salvar() {

        if (validateFields()) {
            String name = txtName.getText().toString();
            String fone = txtPhone.getText().toString();
            String email = txtMail.getText().toString();
            String birthday = txtDate.getText().toString();
            if (phoneLinearLayout.getChildCount() > 0) fone = getPhones(fone);

            if (c == null) c = new Contato();
            c.setNome(name);
            c.setFone(fone);
            c.setEmail(email);
            c.setFavorite(getFavorite());
            c.setBirthday(birthday);

            cDAO.salvaContato(c);

            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();

        } else {
            Toast.makeText(this, "\n" +
                    "28/5000\n" +
                    "Attention, required fields >>  " + "\t" + getString(R.string.nome) + "\t" + getString(R.string.fone), Toast.LENGTH_SHORT) .show();
        }

    }

    public int getFavorite(){
        Switch field_favorite = (Switch) findViewById(R.id.switchFavorite);
        int favorite = 0;
        if (field_favorite.isChecked()) {
            favorite = 1;
        }
        return favorite;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.imgCalendar:
                openDialogCalendar();
                break;
            case R.id.btAddPhone:
                addNewFone(null);
                break;
            case R.id.imageButtonDelNewFone:
                removeViewNewFone(view);
                break;


        }
    }

    public String getPhones(String fone) {

        String fones = fone;

        for (int i = 0; i < phoneLinearLayout.getChildCount(); i++) {
            View view = phoneLinearLayout.getChildAt(i);
            EditText newFone = view.findViewById(R.id.editTextNewFone);
            fones = fones + ";" + newFone.getText().toString();

        }

        return fones;
    }

    private void removeViewNewFone(View v) {

        phoneLinearLayout.removeView((View) v.getParent());
        Toast.makeText(this, "Phone Removed ", Toast.LENGTH_SHORT).show();

    }

    private void addNewFone(String[] fones) {

        LayoutInflater layoutInflater = getLayoutInflater();
        View newPhoneView = null;
        ImageButton imgBtnDel;

        if (null != fones) {

            for (int i = 1; i < fones.length; i++) {

                newPhoneView = layoutInflater.inflate(R.layout.activity_new_phone, null);
                phoneLinearLayout.addView(newPhoneView);

                EditText newFone = newPhoneView.findViewById(R.id.editTextNewFone);
                newFone.setText(fones[i]);

                imgBtnDel = newPhoneView.findViewById(R.id.imageButtonDelNewFone);
                imgBtnDel.setOnClickListener(this);

            }

        } else {

            newPhoneView = layoutInflater.inflate(R.layout.activity_new_phone, null);
            phoneLinearLayout.addView(newPhoneView);

            imgBtnDel = newPhoneView.findViewById(R.id.imageButtonDelNewFone);
            imgBtnDel.setOnClickListener(this);

        }

    }

    private void openDialogCalendar() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        DecimalFormat df = new DecimalFormat("00");

                        txtDate.setText(df.format(day) + "/" + (df.format(month + 1)));
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    public boolean validateFields(){
        if (txtName.getText().toString().trim().equals("") || txtPhone.getText().toString().trim().equals("")) return false;
        return true;
    }

}

