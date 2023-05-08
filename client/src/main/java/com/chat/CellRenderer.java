package com.chat;

import com.messages.User;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

//Load the user list.
public class CellRenderer implements Callback<ListView<User>, ListCell<User>> {
    @Override
    public ListCell<User> call(ListView<User> userListView) {
        ListCell cell = new ListCell<User>(){
            @Override
            protected void updateItem(User user, boolean b){
                super.updateItem(user, b);
                setText(null); setGraphic(null);
                if(user!=null){
                    HBox hBox= new HBox();
                    Text name = new Text(user.getName());
                    hBox.getChildren().addAll(name);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(hBox);
                }
            }

        };
        return cell;
    }
}
