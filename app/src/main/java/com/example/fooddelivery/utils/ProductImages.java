package com.example.fooddelivery.utils;

import android.widget.ImageView;

import com.example.fooddelivery.R;

/**
 * Loads a product photo by its drawable name.
 *
 * <p>The four burger photos are real photographs (Unsplash, free to
 * use - see the photo README in the project files). Their drawable
 * names match the {@code imageName} stored for each product in
 * {@link com.example.fooddelivery.data.ProductCatalog}, for example
 * {@code img_cheeseburger_wendys}.</p>
 *
 * <p><b>Fallback, on purpose:</b> the lookup is done by name at
 * runtime. If a photo file is missing - for example, if the branch
 * was pulled before the JPG files were added to
 * {@code app/src/main/res/drawable} - the placeholder vector
 * {@code img_splash_burger} is shown instead and the app still
 * builds and runs. So the JPGs can be added later without breaking
 * the build.</p>
 */
public final class ProductImages {

    private ProductImages() {
        // Utility class - do not instantiate.
    }

    /** Show the photo for {@code imageName}, or the placeholder if it is missing. */
    public static void setProductImage(ImageView imageView, String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            imageView.setImageResource(R.drawable.img_splash_burger);
            return;
        }
        int resId = imageView.getContext().getResources().getIdentifier(
                imageName, "drawable", imageView.getContext().getPackageName());
        if (resId != 0) {
            imageView.setImageResource(resId);
        } else {
            imageView.setImageResource(R.drawable.img_splash_burger);
        }
    }
}
