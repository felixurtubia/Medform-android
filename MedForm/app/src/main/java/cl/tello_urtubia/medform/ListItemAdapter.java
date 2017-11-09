package cl.tello_urtubia.medform;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import cl.tello_urtubia.medform.Entidades.Paciente;


class ListItemAdapter extends BaseAdapter implements Filterable{
    CustomFilter filter;
    Context c;
    ArrayList<Paciente> pacientes;
    ArrayList<Paciente> filterList;
    public ListItemAdapter(@NonNull Context context, ArrayList<Paciente> pacientes) {
        this.c = context;
        this.pacientes = pacientes;
        this.filterList = pacientes;
    }

    @Override
    public int getCount() {
        return pacientes.size();
    }

    @Override
    public Object getItem(int pos) {
        return pacientes.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pacientes.indexOf(getItem(pos));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflador = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView==null){
            convertView = inflador.inflate(android.R.layout.simple_list_item_1, null);
        }

        String paciente= pacientes.get(position).getName() ;
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(paciente);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new CustomFilter();
        }
        return filter;
    }

    class CustomFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length()>0){
                constraint = constraint.toString().toUpperCase();

                ArrayList<Paciente> filters = new ArrayList<Paciente>();

                for (int i=0; i<filterList.size(); i++){
                    if(filterList.get(i).getName().toUpperCase().contains(constraint)){
                        Paciente p = new Paciente();
                        p.setNombre(filterList.get(i).getNombre());
                        p.setRut(filterList.get(i).getRut());
                        p.setDireccion(filterList.get(i).getDireccion());
                        p.setSexo(filterList.get(i).getSexo());
                        p.setFecha(filterList.get(i).getFecha());

                        filters.add(p);

                    }
                }
                results.count=filters.size();
                results.values = filters;

            } else {
                results.count=filterList.size();
                results.values=filterList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pacientes = (ArrayList<Paciente>) results.values;
            notifyDataSetChanged();


        }
    }
}
