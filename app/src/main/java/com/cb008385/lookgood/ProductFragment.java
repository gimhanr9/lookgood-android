package com.cb008385.lookgood;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import com.cb008385.lookgood.databinding.FragmentProductsBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Adapter.ProductAdapter;
import ViewModel.ProductViewModel;
import listeners.ProductClickListener;
import models.ProductItem;

public class ProductFragment extends Fragment implements ProductClickListener, SearchView.OnQueryTextListener {

    private FragmentProductsBinding fragmentProductsBinding;
    private ProductAdapter productAdapter;
    private ProductViewModel productViewModel;
    private List<String> brandList;
    private List<ProductItem> products =new ArrayList<>();
    private List<ProductItem> productHelper;
    RecyclerView recyclerView;
    ArrayAdapter<String> valueAdapter;
    List<String> priceCondition;




    public ProductFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentProductsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_products,container,false);
        initView();
        return fragmentProductsBinding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String category=ProductFragmentArgs.fromBundle(getArguments()).getCategory();
        getProducts(category);
        setHasOptionsMenu(true);
        fragmentProductsBinding.sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String condition = parent.getItemAtPosition(position).toString();
                if(condition.equals("Brand")){
                    if(brandList!=null) { //check if a list of brands are available
                        valueAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, brandList);
                        fragmentProductsBinding.sortValue.setAdapter(valueAdapter);
                        if (fragmentProductsBinding.sortValue.getVisibility() == View.GONE) {
                            fragmentProductsBinding.sortValue.setVisibility(View.VISIBLE);
                        }

                    }
                }else if(condition.equals("Price")){
                    valueAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, priceCondition);
                    fragmentProductsBinding.sortValue.setAdapter(valueAdapter);
                    if (fragmentProductsBinding.sortValue.getVisibility() == View.GONE) {
                        fragmentProductsBinding.sortValue.setVisibility(View.VISIBLE);
                    }

                }else if(condition.equals("Show All")){
                    getProducts(category);
                    if (fragmentProductsBinding.sortValue.getVisibility() == View.VISIBLE) {
                        fragmentProductsBinding.sortValue.setVisibility(View.GONE);
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        fragmentProductsBinding.sortValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value = parent.getItemAtPosition(position).toString();
                if(value.equals("Highest First")){
                    sortList("Highest",productHelper);


                }else if(value.equals("Lowest First")){
                    sortList("Lowest",productHelper);

                }else{

                    productViewModel.getBrandItems(category,value).observe(getViewLifecycleOwner(), new Observer<List<ProductItem>>() {
                        @Override
                        public void onChanged(List<ProductItem> productItems) {
                            products.clear();
                            products.addAll(productItems);
                            productHelper=products;
                            productAdapter.notifyDataSetChanged();
                        }
                    });
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initView() {
        priceCondition=new ArrayList<>();
        priceCondition.add("Lowest First");
        priceCondition.add("Highest First");
        recyclerView= fragmentProductsBinding.menTopRecyclerview;
        productViewModel=new ViewModelProvider(getActivity()).get(ProductViewModel.class);
        recyclerView.setHasFixedSize(true);
        productAdapter =new ProductAdapter(products,this);
        recyclerView.setAdapter(productAdapter);
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.sort_array,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentProductsBinding.sort.setAdapter(arrayAdapter);

    }

    private void getProducts(String category){
        fragmentProductsBinding.setIsLoading(true);

        productViewModel.getBrands().observe(getViewLifecycleOwner(), new Observer<List<String>>() { //get list of available brands
            @Override
            public void onChanged(List<String> list) {
                brandList=list; //add brands to list

            }
        });
        productViewModel.getProductData(category).observe(getViewLifecycleOwner(), new Observer<List<ProductItem>>() {
            @Override
            public void onChanged(List<ProductItem> productItems) {
                fragmentProductsBinding.setIsLoading(false);
                if(products.size()>0){
                    products.clear();
                }
                products.addAll(productItems);
                productHelper=products;
                productAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.product_menu,menu);
        final MenuItem item=menu.findItem(R.id.search_item);
        final SearchView searchView=(SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                productAdapter.setSearchResult(productHelper);//changed this
                return true;
            }
        });

    }

    @Override
    public void onProductClicked(ProductItem productItem) {
        float FPrice=(float) productItem.getPrice();
        NavDirections action=NavGraphDirections.actionGlobalProductDetailsFragment(productItem.getId(),productItem.getImageUrl(),productItem.getName(),productItem.getTitle(),
                productItem.getBrand(),productItem.getDescription(),productItem.getSmall(),productItem.getMedium(),productItem.getLarge(),
                productItem.getXlarge(),productItem.getXxl(),FPrice);
        Navigation.findNavController(getView()).navigate(action);

    }

    public void sortList(String condition, List<ProductItem> itemList){

        List<ProductItem> tempList=new ArrayList<>(itemList);

        if(condition.equals("Lowest")) {
            Collections.sort(tempList, new Comparator<ProductItem>() {
                @Override
                public int compare(ProductItem o1, ProductItem o2) {
                    return Double.compare(o1.getPrice(), o2.getPrice());

                }
            });

        }else if(condition.equals("Highest")){
            Collections.sort(tempList, new Comparator<ProductItem>() {
                @Override
                public int compare(ProductItem o1, ProductItem o2) {
                    return Double.compare(o2.getPrice(), o1.getPrice());
                }
            });
        }
        products.clear();
        products.addAll(tempList);
        productAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<ProductItem> filteredModelList = filter(productHelper, newText);
        productAdapter.setSearchResult(filteredModelList);
        return true;
    }

    private List<ProductItem> filter(List<ProductItem> models, String query) {
        if(query.length()==0){
            return productHelper;
        }else {
            query = query.toLowerCase();
            final List<ProductItem> filteredModelList = new ArrayList<>();
            for (ProductItem model : models) {
                final String text = model.getName().toLowerCase();
                if (text.contains(query)) {
                    filteredModelList.add(model);
                }
            }
            return filteredModelList;
        }
    }
}