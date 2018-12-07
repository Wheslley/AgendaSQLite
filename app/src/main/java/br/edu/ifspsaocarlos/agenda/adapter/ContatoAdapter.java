package br.edu.ifspsaocarlos.agenda.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.ifspsaocarlos.agenda.data.ContatoDAO;
import br.edu.ifspsaocarlos.agenda.model.Contato;
import br.edu.ifspsaocarlos.agenda.R;

import java.util.List;


public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder> {

    private static List<Contato> contatos;
    private Context context;
    private ContatoDAO contatoDAO;

    private static ItemClickListener clickListener;

    public ContatoAdapter(List<Contato> contatos, Context context) {
        this.contatos = contatos;
        this.context = context;
    }

    @Override
    public ContatoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.contato_celula, parent, false);
        return new ContatoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ContatoViewHolder holder, int position) {

       holder.nome.setText(this.contatos.get(position).getNome());

       if (this.contatos.get(position).getFavorite() == 1)holder.favoriteIcon.setChecked(true);
       else holder.favoriteIcon.setChecked(false);

    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }


    public  class ContatoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView nome;
        CheckBox favoriteIcon;

        ContatoViewHolder(final View view) {

            super(view);
            nome = (TextView) view.findViewById(R.id.nome);
            favoriteIcon = (CheckBox) view.findViewById(R.id.favorite_start_check_box);

            view.setOnClickListener(this);

            favoriteIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) favoriteIcon.setButtonDrawable(R.drawable.ic_star_yellow);
                    else favoriteIcon.setButtonDrawable(R.drawable.ic_star_border_yellow);

                    auditingFavorites(getAdapterPosition(), isChecked==true?1:0);

                }
            });
        }

        @Override
        public void onClick(View view) {

            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition());
        }
    }

    void auditingFavorites(int position, int indexFavorites){

        Contato contato = new Contato();
        contato.setId(this.contatos.get(position).getId());
        contato.setFavorite(indexFavorites);

        this.contatoDAO = new ContatoDAO(this.context);
        this.contatoDAO.auditingFavorites(contato);
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

   /* public interface ItemFavoriteChange {
        void onFavoriteChange(int position,int favorito);
    }
*/

}
