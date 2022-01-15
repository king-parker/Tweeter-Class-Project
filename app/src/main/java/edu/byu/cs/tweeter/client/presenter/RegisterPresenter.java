package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends BasePresenter<RegisterPresenter.View> implements UserService.RegisterObserver {

    public interface View extends PresenterView {
        void navigateToUser(User user);
    }

    public RegisterPresenter(View view) {
        super(view);
    }

    public void register(String firstName, String lastName, String alias, String password,
                         Drawable image) {
        view.clearErrorMessage();
        view.clearInfoMessage();

        String message = validateRegistration(firstName, lastName, alias, password, image);
        if (message == null) {
            view.displayInfoMessage("Registering...");

            // Send the register request
            String imageBytesBase64 = imageToByteArray(image);
            getUserService().register(firstName, lastName, alias, password, imageBytesBase64, this);
        }
        else {
            view.displayErrorMessage("Registration failed: " + message);
        }
    }

    private String validateRegistration(String firstName, String lastName, String alias, String password,
                                        Drawable image) {
        if (firstName.length() == 0) {
            return "First Name cannot be empty.";
        }
        if (lastName.length() == 0) {
            return "Last Name cannot be empty.";
        }
        if (alias.length() == 0) {
            return "Alias cannot be empty.";
        }
        if (alias.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (alias.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }

        if (image == null) {
            return "Profile image must be uploaded.";
        }
        return null;
    }

    // Convert image to byte array.
    private String imageToByteArray(Drawable imageDrawable) {
        Bitmap image = ((BitmapDrawable) imageDrawable).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.NO_WRAP);
    }

    @Override
    public void handleSuccess(User user, AuthToken authToken) {
        view.navigateToUser(user);
        view.clearErrorMessage();
        view.displayInfoMessage("Hello " + user.getName());
    }

    @Override
    public void handleFailure(String message) {
        view.displayErrorMessage("Registration failed: " + message);
    }

    @Override
    public void handleException(Exception ex) {
        view.displayErrorMessage("Registration failed: " + ex.getMessage());
    }
}
