package be.ehb.myrecipe.util;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import be.ehb.myrecipe.R;
import be.ehb.myrecipe.model.Recipe;
import be.ehb.myrecipe.model.RecipeViewModel;

import static java.security.AccessController.getContext;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> implements Filterable {

    private List<Recipe> allRecipes;
    private List<Recipe> selectedRecipes;
    private FragmentActivity fragmentActivity;

    class RecipeViewHolder extends RecyclerView.ViewHolder{
        final TextView r_name;
        final ImageView imageView;
        final CardView card;
        final Button btnDetails;
        final ImageButton btnDelete;

            RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            r_name = itemView.findViewById(R.id.tv_recipe_name);
            card = itemView.findViewById(R.id.recipe_card);
            btnDetails = itemView.findViewById(R.id.btn_recipe_detail);
            btnDetails.setOnClickListener(getDetailListener);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            btnDelete.setOnClickListener(deleteListener);
            imageView = itemView.findViewById(R.id.iv_card_recipe);
        }

        private View.OnClickListener deleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Recipe delete = selectedRecipes.get(position);
                RecipeViewModel model = new ViewModelProvider(fragmentActivity).get(RecipeViewModel.class);
                model.deleteRecipe(delete);
                notifyDataSetChanged();
                Toast.makeText(fragmentActivity,"Recipe deleted succesfully !",Toast.LENGTH_SHORT).show();
            }
        };

        private View.OnClickListener getDetailListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                Recipe toGet = selectedRecipes.get(position);
                Bundle data = new Bundle();
                data.putSerializable("InsertedRecipe", toGet);
                Navigation.findNavController(itemView).navigate(R.id.action_to_new_recipe_fragment, data);
            }
        };

    }

    public RecipeAdapter(FragmentActivity fragmentActivity){
        this.allRecipes = new ArrayList<>();
        this.selectedRecipes = new ArrayList<>();
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cv_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = selectedRecipes.get(position);
        holder.r_name.setText(recipe.getRecipeName());
        Picasso.get()
                .load(recipe.getRecipeImage())
                .resize(600, 500)
                .into(holder.imageView);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Recipe> searchList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                searchList.addAll(allRecipes);
            }else{
                for(Recipe item : allRecipes){
                    if(item.getRecipeName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                         searchList.add(item);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = searchList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
           selectedRecipes.clear();
           selectedRecipes.addAll((ArrayList)filterResults.values);
           notifyDataSetChanged();

        }
    };

    @Override
    public int getItemCount() {
        return selectedRecipes.size();
    }

    public void addRecipes(List<Recipe> recipes){
        selectedRecipes.clear();
        allRecipes.clear();
        selectedRecipes.addAll(recipes);
        allRecipes.addAll(recipes);

    }








}
