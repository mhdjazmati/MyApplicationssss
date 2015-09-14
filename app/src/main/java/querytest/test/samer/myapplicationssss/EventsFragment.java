package querytest.test.samer.myapplicationssss;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SAMER on 29-Jul-15.
 */
public class EventsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        list = (ListView) view.findViewById(R.id.listView);
        itemsCountEditTxt = (EditText) view.findViewById(R.id.edtTxt);
        Button btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyBtn_Clicked();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    EditText itemsCountEditTxt;
    ListView list;
    //int rowsCount;
    ArrayList<String> ar;
    public int editTextsCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //rowsCount = 4;
        editTextsCount = 1;

        ar = new ArrayList<>(5);
        ar.add("Zekr");
        ar.add("Salah");
        ar.add("Estegfar");
        ar.add("Syam");
        ar.add("Kiyam");

    }

    public void applyBtn_Clicked() {

        editTextsCount = Integer.parseInt(itemsCountEditTxt.getText().toString());

        final MyAdapter adapter = new MyAdapter(getActivity(), 3);
        list.setAdapter(adapter);
    }

    class ViewHolder {
        LinearLayout linearLayout;
        TextView art;
        LinearLayout base_content;
        ImageView image;
        TextView titleTextView;
        TextView artistTextView;
        TextView albumTextView;
        TextView descriptionTextView;
        CheckBox checkBox;


        ViewHolder(View view) {
            titleTextView = (TextView) view.findViewById(R.id.title);

         /*   base_content = (LinearLayout) findViewById(R.id.base_content);
            image = (ImageView) view.findViewById(R.id.imgViewIcon);
            titleTextView = (TextView) view.findViewById(R.id.title);
            artistTextView = (TextView) view.findViewById(R.id.txtArtist);
            albumTextView = (TextView) view.findViewById(R.id.txtAlbum);
            descriptionTextView = (TextView) view.findViewById(R.id.txtDescrip);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
*/
            linearLayout = (LinearLayout) view.findViewById(R.id.base_content);
            //art = (TextView) view.findViewById(R.id.txtArtist);

            for (int i = 0; i < ar.size(); i++) {
                //float gg = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()));

                param.setMargins(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()),
                        0,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()),
                        0);
                param.gravity = Gravity.CENTER;
                LinearLayout linearContent = new LinearLayout(getActivity());
                //linearContent.setLayoutParams(param);

                ImageView pic = new ImageView(getActivity());
                pic.setImageResource(R.drawable.options);
                TextView TV = new TextView(getActivity());
                //pic.setLayoutParams(param);
                //TV.setLayoutParams(param);
                TV.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                TV.setText(ar.get(i) + " : ");
                TV.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
                EditText edt = new EditText(getActivity());
                edt.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                edt.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
                linearContent.addView(pic, param);
                linearContent.addView(TV);
                linearContent.addView(edt);
                linearLayout.addView(linearContent);

                 /*   LinearLayout.Layoutparams params = (LinearLayout.LayoutParams)button.getLayoutParams();
                    params.setMargins(5, 5, 5, 5);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    button.setLayoutParams(params);*/

/*
                    EditText edt =new EditText(MainActivity.this);
                    edt.setText("-");

                    linearLayout.addView(edt);*/
            }

        }

    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        int rowsCount;


        MyAdapter(Context c, int rowsCount) {
            super(c, R.layout.list_item, R.id.title);
            this.context = c;
            this.rowsCount = rowsCount;

        }


        @Override
        public int getCount() {
            return rowsCount;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;


            if (row == null) { // the row is being created for the 1st time
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder(row);
                row.setTag(holder);
            } else

            {
                holder = (ViewHolder) row.getTag();
            }
            // GregorianCalendar gc = new GregorianCalendar();
            // gc.add(Calendar.DATE, 1);

            //holder.art.setText("ffff"+position);
            // holder.linearLayout
            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();

            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            String todayAsString = dateFormat.format(today);
            String tomorrowAsString = dateFormat.format(tomorrow);

            //System.out.println(todayAsString);
            //System.out.println(tomorrowAsString);
            //String datestring = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new java.util.Date(System.currentTimeMillis()));
            //String datestring1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(gc);
            if (position == 0)
                holder.titleTextView.setText(todayAsString);
            if (position == 1)
                holder.titleTextView.setText(tomorrowAsString);
                return row;
        }
    }
/*

    class SongsViewHolder {
        LinearLayout base_content;
        ImageView image;
        TextView titleTextView;
        TextView artistTextView;
        TextView albumTextView;
        TextView descriptionTextView;
        CheckBox checkBox;

        SongsViewHolder(View view) {

            base_content = (LinearLayout) findViewById(R.id.base_content);
            image = (ImageView) view.findViewById(R.id.imgViewIcon);
            titleTextView = (TextView) view.findViewById(R.id.title);
            artistTextView = (TextView) view.findViewById(R.id.txtArtist);
            albumTextView = (TextView) view.findViewById(R.id.txtAlbum);
            descriptionTextView = (TextView) view.findViewById(R.id.txtDescrip);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        }

    }

    class SongsAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<Song> songs;

        SongsAdapter(Context c, ArrayList<Song> songs) {
            super(c, R.layout.list_view_row, R.id.title);
            this.context = c;
            this.songs = songs;


        }

        @Override
        public int getCount() {
            return 10;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            SongsViewHolder holder;


            if (row == null) { // the row is being created for the 1st time
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.list_view_row, parent, false);
                holder = new SongsViewHolder(row);
                row.setTag(holder);
            } else

            {
                holder = (SongsViewHolder) row.getTag();
            }


            //GIVE VALUES TO THE HOLDER
            holder.checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    selectedSongs.get(position).isSelected = isChecked;
                }
            });


            // Putting some values so the TextViews won't be left blank
            if (selectedSongs.get(position).Title == null || selectedSongs.get(position).Title.trim().equals("")) {
                selectedSongs.get(position).Title = FileDirectoryHelper.getFileName(selectedSongs.get(position).FilePath + "(No Title");
            }
            if (selectedSongs.get(position).Artist == null || selectedSongs.get(position).Artist.trim().equals(""))
                selectedSongs.get(position).Artist = "unknown";
            if (selectedSongs.get(position).Album == null || selectedSongs.get(position).Album.trim().equals(""))
                selectedSongs.get(position).Album = "unknown";


            // Preparing holder
            holder.albumTextView.setText(selectedSongs.get(position).Album);
            holder.artistTextView.setText(selectedSongs.get(position).Artist);
            holder.titleTextView.setText(selectedSongs.get(position).Title);
            holder.descriptionTextView.setText("Some description");
            holder.image.setImageBitmap(selectedSongs.get(position).thumbnail);
            holder.checkBox.setChecked(selectedSongs.get(position).isSelected);


            return row;
        }
    }

*/


}
