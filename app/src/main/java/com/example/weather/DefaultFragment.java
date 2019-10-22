package com.example.weather;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DefaultFragment extends Fragment {

    TextView txt_dashname,txt_wename,txt_mintemp,txt_maxtemp,txt_acttemp,txt_humidity,txt_predect, txt_more_info;
    ImageView img_we;
    ArrayList<ConsolidatedWeather> weathers;
    ArrayList<Source> sources;
    RecyclerView recyclerView ;
    int cityId;
    String city, bbc_url;

    public DefaultFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(this.getArguments() != null){
            cityId = this.getArguments().getInt("cityid");
        }else{
            cityId = 3534;
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_default, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_dashname = view.findViewById(R.id.dash_name);
        txt_wename = view.findViewById(R.id.dash_wename);
        img_we = view.findViewById(R.id.dash_actimg);
        txt_mintemp = view.findViewById(R.id.txt_mintemp);
        txt_maxtemp = view.findViewById(R.id.txt_maxtemp);
        txt_acttemp = view.findViewById(R.id.txt_actemp);

        txt_humidity = view.findViewById(R.id.txt_humidity);
        txt_predect = view.findViewById(R.id.txt_prec);
        txt_more_info = view.findViewById(R.id.txt_moreinfo);



        recyclerView = view.findViewById(R.id.recycleV);

        getWeather();

        txt_more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("city", city);
                if(city.equals("Calgary")){
                    bundle.putString("bbc_url", bbc_url + "5913490");
                } else if(city.equals("Toronto")){
                    bundle.putString("bbc_url", bbc_url + "6167865");
                } else if(city.equals("Edmonton")){
                    bundle.putString("bbc_url", bbc_url + "5946768");
                } else if(city.equals("Vancouver")){
                    bundle.putString("bbc_url", bbc_url + "6173331");
                } else {
                    bundle.putString("bbc_url", bbc_url + "6077243");
                }
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                final WebFragment webFragment = new WebFragment();
                fragmentTransaction.add(R.id.host_fragment, webFragment);
                fragmentTransaction.addToBackStack(null);
                webFragment.setArguments(bundle);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void getWeather()
    {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<CitiesWeather> call = service.getCitiesWeatherCall(cityId);

        //System.out.println("ID: " + getArguments().getInt("cityid"));

        call.enqueue(new Callback<CitiesWeather>() {
            @Override
            public void onResponse(Call<CitiesWeather> call, Response<CitiesWeather> response) {

                System.out.println("Response Called!");

                CitiesWeather citiesWeather = response.body();

                weathers = new ArrayList<>(citiesWeather.getConsolidatedWeather());

                sources = new ArrayList<>(citiesWeather.getSources());

                System.out.println("Check Size :"+weathers.size());

                city = citiesWeather.getTitle();
                bbc_url = sources.get(0).getUrl();
                txt_dashname.setText(citiesWeather.getTitle().toUpperCase());

                setData(weathers);
            }

            @Override
            public void onFailure(Call<CitiesWeather> call, Throwable t) {

                System.out.println("Failure Called! :" +t.getMessage());
            }
        });

    }

    public void setData(ArrayList<ConsolidatedWeather> wearray)
    {
        System.out.println("Size From method :"+wearray.size());
        txt_wename.setText(wearray.get(0).getWeatherStateName());
        setImage(img_we,getWeImage(wearray.get(0).getWeatherStateAbbr()));

        String thetemp = String.format("%.2f",wearray.get(0).getTheTemp());
        String mintemp = String.format("%.2f",wearray.get(0).getMinTemp());
        String maxtemp = String.format("%.2f",wearray.get(0).getMaxTemp());

        txt_mintemp.setText(mintemp);
        txt_maxtemp.setText(maxtemp);
        txt_acttemp.setText(thetemp);

        txt_humidity.setText("Humidity : "+wearray.get(0).getHumidity().toString());
        txt_predect.setText("Predictability : "+wearray.get(0).getPredictability().toString()+"%");

        initView(wearray);

    }

    public String getWeImage(String code)
    {
        return "https://www.metaweather.com/static/img/weather/png/"+code+".png";
    }

    public void setImage(ImageView img,String link)
    {
        Picasso.get().load(link).into(img);
    }

    public void initView(ArrayList<ConsolidatedWeather> wearray)
    {
        wearray.remove(0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManager);
        WeatherAdapter adapter = new WeatherAdapter(wearray,getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
    }


}
