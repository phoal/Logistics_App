package gui;

import controller.Controller;
import controller.UserDatabase;
import gui.dialogs.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import model.User;

import java.util.List;

/**
 * Displays the account management operations such changing a logged-in user's username or password.
 * More options are displayed if the logged-in user is an admin, where they can promote/demote a user to a
 * manager/clerk, delete users and create new accounts.
 *
 * @author Jonathan Young, Prashant Bhikhu
 */
public class UserAccountManagement implements EventHandler<MouseEvent> {
	private Controller controller;
	private Scene scene = null;
	/*Loads the image from the Resource folder and turns it to imageView to be used as an icon
	with the button*/

	 public UserAccountManagement(Controller controller) {
		this.controller = controller;

		BorderPane root = new BorderPane();

		//Load the images for the 4 buttons

		//Button Creation
		 Button changeUsernameButton = new Button ("Change Username", null);
		 changeUsernameButton.setPadding(new Insets(4, 20, 4, 20));
		 changeUsernameButton.setMaxSize(250, 100);
		 changeUsernameButton.setWrapText(true);
		 changeUsernameButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this);

		Button changePasswordButton = new Button ("Change Password", null);
         changePasswordButton.setPadding(new Insets(4, 20, 4, 20));
		changePasswordButton.setMaxSize(250, 100);
		changePasswordButton.setWrapText(true);
		changePasswordButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this);

		Button newAccountButton = new Button ("Create new User Account", null);
         newAccountButton.setPadding(new Insets(4, 20, 4, 20));
		newAccountButton.setMaxSize(250, 100);
		newAccountButton.setWrapText(true);
		//Mouse event handling for creating a New Account
		newAccountButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this);


		Button promoteAccountButton = new Button ("Change User Accounts Permissions", null);
         promoteAccountButton.setPadding(new Insets(4, 20, 4, 20));
		promoteAccountButton.setMaxSize(250, 100);
		promoteAccountButton.setWrapText(true);
		//Mouse event handling for Promoting an Account, only appears if the
		//user has proper permissions
		promoteAccountButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this);
		
		
		Button deleteAccountButton = new Button("Delete User Accounts");
         deleteAccountButton.setPadding(new Insets(4, 20, 4, 20));
		deleteAccountButton.setMaxSize(250, 100);
		deleteAccountButton.setWrapText(true);
		//Mouse event handling for Deleting Account
		deleteAccountButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this);
		
		
		Button backButton = new Button ("Back");
		backButton.setMaxSize(150, 100);
		backButton.setWrapText(true);
		backButton.setStyle("fx-scale-y: 1.0;");
		backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this);
		
		
		Button logoutButton = new Button ("Logout");
		logoutButton.setMaxSize(150, 100);
		logoutButton.setWrapText(true);
         logoutButton.setStyle("fx-scale-y: 1.0;");
		logoutButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this);
		
		

		scene = new Scene(root, 600, 400);
		VBox box = new VBox();

		//Add all buttons to a container. Place the container on the center of the BorderPane
		box.getChildren().addAll(changeUsernameButton, changePasswordButton);
		if(controller.getLoggedInUser().isManager()) {
            box.getChildren().addAll(newAccountButton, deleteAccountButton, promoteAccountButton);
        }
		box.setAlignment(Pos.CENTER);

		//This adds spaces when items are being added (top,left, bottom, right)
        VBox.setMargin(changeUsernameButton, new Insets(10,20,10,20));
        VBox.setMargin(changePasswordButton, new Insets(10,20,10,20));
        VBox.setMargin(newAccountButton, new Insets(10,20,10,20));
        VBox.setMargin(promoteAccountButton, new Insets(10,20,10,20));
        VBox.setMargin(deleteAccountButton, new Insets(10,20,10,20));
		
		VBox box2 = new VBox(10);
		box2.setMinHeight(100);
		box2.getChildren().addAll(backButton, logoutButton);
		box2.setAlignment(Pos.BOTTOM_RIGHT);
		box2.setPadding(new Insets(10));

		root.setCenter(box);
		root.setBottom(box2);

	}

	public Scene scene() {
			return scene;
	}


	@Override
	public void handle(MouseEvent event) {
		System.out.println(event.toString());

		Button buttonClicked = (Button) event.getSource();
		switch (buttonClicked.getText()) {
			case "Change Username":
			    onChangeUsernameButtonClicked();
				break;
			case "Change Password":
			    oChangePasswordButtonClicked();
				break;
			case "Create new User Account":
				onCreateAccountButtonClicked();
				break;
			case "Delete User Accounts":
				onDeleteAccountButtonClicked();
				break;
			case "Change User Accounts Permissions":
			    onChangeUserAccountPermissionsButtonClicked();
				break;
			case "Back":
				controller.handleEvent(Controller.MAINSCREEN);
				break;
			case "Logout":
				LogoutDialog logoutDialog = new LogoutDialog(controller);
				logoutDialog.display();
				break;
			default:
				throw new IllegalArgumentException("Event passed into handler method has not been handled correctly.");
		}
	}

    private void onChangeUsernameButtonClicked() {
        ChangeUsernameDialog changeUsernameDialog = new ChangeUsernameDialog(controller);
        changeUsernameDialog.display();

        if(changeUsernameDialog.isCancelled()) {
            return;
        }

        try {
            controller.getUserDatabase().changeUserUsername(controller.getLoggedInUser().getUsername(), changeUsernameDialog.getNewUsername());
            //TODO: Update All events, change user field of all events, to new username.
            AlertDialog.display("Change Username Success", "You have now changed your username. Please login again with your your new username.");
            controller.logout();
        } catch (UserDatabase.UserDatabaseException e) {
            AlertDialog.display("Change Username Error", e.getMessage());
            onChangeUsernameButtonClicked();
        }
    }

    private void oChangePasswordButtonClicked() {
        ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(controller);
        changePasswordDialog.display();

        if(changePasswordDialog.isCancelled()) {
            return;
        }

        try {
            controller.getUserDatabase().changeUserPassword(controller.getLoggedInUser().getUsername(), changePasswordDialog.getNewPassword());
            AlertDialog.display("Change Password Success", "You have now changed your password. Please login again with your your new password.");
            controller.logout();
        } catch (UserDatabase.UserDatabaseException e) {
            AlertDialog.display("Change Password Error", e.getMessage());
            oChangePasswordButtonClicked();
        }
    }

    private void onCreateAccountButtonClicked() {
        CreateAccountDialog createAccountDialog = new CreateAccountDialog(controller);
        createAccountDialog.display();

        if(createAccountDialog.isCancelled()) {
            return;
        }

        try {
            controller.getUserDatabase().addUser(createAccountDialog.getUsername(), createAccountDialog.getPassword(), false);
        } catch (UserDatabase.UserDatabaseException e) {
            AlertDialog.display("Create Account Error", e.getMessage());
        }
    }

	private void onDeleteAccountButtonClicked() {
		DeleteUserAccountsDialog deleteUserAccountsDialog = new DeleteUserAccountsDialog(controller);
		deleteUserAccountsDialog.display("Delete Account", "delete");

		// Ends the delete operation if the user does not want to continue.
		if (deleteUserAccountsDialog.isCancelled()) {
			return;
		}

		// Attempt to delete users
		try {
			// The indicator that tells us whether the user is deleting themselves, and whether they went through with it.
			// 0 = Logged-in User was not selected for deletion.
			// 1 = Logged-in User was selected Selected for deletion, Accepted for deletion
			// 2 = Logged-in User was selected Selected for deletion, Rejected for deletion
			int deletionStatus_LoggedInUser = 0;

			for (Object o : deleteUserAccountsDialog.getSelectedIndices()) {
				String username = (String) deleteUserAccountsDialog.getListItems().get((Integer) o);

				// Handling User trying to delete themselves
				if (username.equals(controller.getLoggedInUser().getUsername())) {
					List<User> users = controller.getUserDatabase().getUsers();
					List<User> managers = controller.getUserDatabase().getManagers();
					// isLoggedInUserManager is ignored because it is implied that if they have access to this function
					// then they are indeed a an manager
					// boolean isLoggedInUserManager = controller.getLoggedInUser().isManager();

					// User is the only manager
					if (/*isLoggedInUserManager && */managers.size() == 1) {
						// Reject this particular deletion as user should not be able delete themselves if they are the only manager
						AlertDialog.display("User Deletion Error", "You cannot delete yourself as you are the only manager on the system. All other selected deletions will continue.");
						deletionStatus_LoggedInUser = 2;

						// When the currently logged in user is only user in UserDatabase, ignore all success messages
						// as they are not suitable for this case.
						if (users.size() == 1) {
							return;
						}

					// User is a manager, but other managers exists
					} else if (/*isLoggedInUserManager && */managers.size() > 1) {
						// Generally we allow deletion operation with confirmation, but first we have to check that
						// the the logged-in user is not deleting all accounts or all manangers, otherwise we will
						// be locked out of the program.
						if(deleteUserAccountsDialog.getSelectedIndices().size() == deleteUserAccountsDialog.getListItems().size()) {
							// if the above is true, do not allow the user to deletion of own account
							AlertDialog.display("User Deletion Error", "You cannot delete yourself when deleting all other users on the system. All other selected deletions will continue.");
							deletionStatus_LoggedInUser = 2;
							continue;
						}

						boolean isAllSelectedManagers = true;
						for (Object o2 : deleteUserAccountsDialog.getSelectedIndices()) {
							String username2 = (String) deleteUserAccountsDialog.getListItems().get((Integer) o2);
							User user2 = controller.getUserDatabase().getUser(username2);

							if(!user2.isManager()) {
								isAllSelectedManagers = false;
								break;
							}
						}

						if(isAllSelectedManagers) {
							AlertDialog.display("User Deletion Error", "You cannot delete yourself when deleting all other managers on the system. All other selected deletions will continue.");
							deletionStatus_LoggedInUser = 2;
							continue;
						}


						// Allow deletion operation with confirmation, user can delete themselves as other managers exist
						ConfirmDialog confirmDialog = new ConfirmDialog();
						confirmDialog.display("User Deletion Confirmation", "Are you sure you " +
								"want to delete your own account?"
								+ System.lineSeparator() + System.lineSeparator() +
								"If you choose Okay, all selected deletions will continue, then at the end of the " +
								"deletion process, you will be logged out and will no longer be able to " +
								"use this system. If you cancel, all other selected deletions will continue but" +
								" your own account will remain in the system.");

						// Verify confirmation from user
						if (confirmDialog.confirm) {
							controller.getUserDatabase().deleteUser(username);
							deletionStatus_LoggedInUser = 1;
						} else {
							// User rejected the deletion of their own account
							deletionStatus_LoggedInUser = 2;
							//continue;
						}
					}
				} else {
					controller.getUserDatabase().deleteUser(username);
				}
			}
			// Display success message after all deletions occcured
			AlertDialog.display("User Deletion Success", "All selected users" + (deletionStatus_LoggedInUser == 2 ? " (except the current user)" : "") + " were successfully deleted.");

			// If user deleted themselves, logged them out now
			if(deletionStatus_LoggedInUser == 1) {
				controller.logout();
			}
		} catch (UserDatabase.UserDatabaseException e) {
			// Display error if something when wrong
			AlertDialog.display("User Deletion Error", e.getMessage());
		}
	}

    private void onChangeUserAccountPermissionsButtonClicked() {
        ChangeUserPermissionsDialog changeUserPermissionsDialog = new ChangeUserPermissionsDialog(controller);
        changeUserPermissionsDialog.display();

        if(changeUserPermissionsDialog.isCancelled()) {
            return;
        }

        try {
            List<User> users = controller.getUserDatabase().getUsers();
            List<User> managers = controller.getUserDatabase().getManagers();

            //checkinf if changing your own permissions or not, and whether your the only manager or not.
            if(changeUserPermissionsDialog.getUsername().equals(controller.getLoggedInUser().getUsername()) && managers.size() == 1) {
                // if user is demoting themselves to clerk, reject this operation as they are the only manager.
                if(!changeUserPermissionsDialog.getIsManager()) {
                    AlertDialog.display("Change User Permission Error", "You cannot make be a Clerk as you are the only manager on the system.");
                    return;
                }
            } else {
                controller.getUserDatabase().changeUserPermission(changeUserPermissionsDialog.getUsername(), changeUserPermissionsDialog.getIsManager());
            }

            if(changeUserPermissionsDialog.getUsername().equals(controller.getLoggedInUser().getUsername())) {
                AlertDialog.display("Change User Permission Success", "You have now changed your own permissions to be a " + (changeUserPermissionsDialog.getIsManager() ? "Manager" : "Clerk") + ". Please login again.");
                controller.logout();
            } else {
                AlertDialog.display("Change User Permission Success", "You have successfully changed \"" + changeUserPermissionsDialog.getUsername() + "\" to be a " + (changeUserPermissionsDialog.getIsManager() ? "Manager" : "Clerk") + ".");
            }
        } catch (UserDatabase.UserDatabaseException e) {
            AlertDialog.display("Change User Permission Error", e.getMessage());
        }
    }
}
