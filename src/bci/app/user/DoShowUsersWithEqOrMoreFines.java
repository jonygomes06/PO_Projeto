package bci.app.user;

import bci.core.LibraryManager;
import bci.core.user.User;
import pt.tecnico.uilib.menus.Command;

import java.util.Collection;
import java.util.Collections;

class DoShowUsersWithEqOrMoreFines extends Command<LibraryManager> {

    DoShowUsersWithEqOrMoreFines(LibraryManager receiver) {
        super("Apresenta utentes com dívida maior", receiver);
        addIntegerField("fine", "Dívida base: ");
    }

    @Override
    protected final void execute() {
        int fine = integerField("fine");
        Collection<User> usersWithFines = _receiver.getLibrary().getUsersWithEqOrMoreFines(fine);
        _display.popup(usersWithFines.size());
        _display.popup(usersWithFines.stream().map(User::getFineDescription).toList());
    }
}