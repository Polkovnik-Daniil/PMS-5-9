package be.ehb.myrecipe.fragments.recipe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import be.ehb.myrecipe.R;
import be.ehb.myrecipe.model.Recipe;
import be.ehb.myrecipe.model.RecipeViewModel;

import static android.app.Activity.RESULT_OK;

public class AddRecipeFragment extends Fragment {

    private EditText etRecipeName, etRecipeIngredients, etRecipeDescription;
    private Recipe selected;
    private FragmentActivity fragmentActivity;
    private Button btnSaveRecipe;
    private Button btnLoadImage;
    private static final int PICK_IMAGE_REQUEST = 0;
    private Uri imageUri;
    private ImageView loadImageView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentActivity = (FragmentActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_recipe, container, false);
        selected = (getArguments() != null)?(Recipe) getArguments().getSerializable("InsertedRecipe"):null;
        etRecipeName = view.findViewById(R.id.et_recipe_name);
        etRecipeIngredients = view.findViewById(R.id.et_recipe_ingredients);
        etRecipeDescription = view.findViewById(R.id.et_recipe_description);
        btnLoadImage = view.findViewById(R.id.btn_load_image);
        btnSaveRecipe = view.findViewById(R.id.btn_recipe_save);
        loadImageView = view.findViewById(R.id.imageLoadView);


        if(selected != null) {
            etRecipeName.setText(selected.getRecipeName());
            etRecipeIngredients.setText(selected.getRecipeIngredients());
            etRecipeDescription.setText(selected.getRecipeDescription());
            Picasso.get()
                    .load(selected.getRecipeImage())
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_error)
                    .into(loadImageView);
        }


        btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        btnSaveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeViewModel viewModel = new ViewModelProvider(fragmentActivity).get(RecipeViewModel.class);
                try{
                if(etRecipeName.getText().toString().matches("") || etRecipeIngredients.getText().toString().matches("") || etRecipeDescription.getText().toString().matches("") || loadImageView.getDrawable() == null) {
                    Toast.makeText(getContext(), "Please fill in all fields !", Toast.LENGTH_SHORT).show();
                }else {
                    if (selected == null) {
                        Recipe recipe = new Recipe(etRecipeName.getText().toString(), etRecipeIngredients.getText().toString(), etRecipeDescription.getText().toString(), imageUri.toString());
                        viewModel.insertRecipe(recipe);
                        Toast.makeText(getContext(), "Recipe added succesfully !", Toast.LENGTH_SHORT).show();
                    } else {
                        selected.setRecipeName(etRecipeName.getText().toString());
                        selected.setRecipeIngredients(etRecipeIngredients.getText().toString());
                        selected.setRecipeDescription(etRecipeDescription.getText().toString());
                        selected.setRecipeImage(imageUri.toString());
                        viewModel.updateRecipe(selected);
                    }
                    Navigation.findNavController(v).navigate(R.id.action_addRecipeFragment_to_recipesFragment);
                }
                }catch (Exception e){
                    Toast.makeText(getContext(),"ERROR: " + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imageUri = data.getData();
                Picasso.get().load(imageUri).into(loadImageView);
            }
    }

    public void openImageChooser(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(i.ACTION_OPEN_DOCUMENT);
        startActivityForResult(i.createChooser(i,"Choose an image"),0);
    }

}