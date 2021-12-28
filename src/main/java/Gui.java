import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Gui {

    private final TrayIcon trayIcon;

    public Gui() {
        try {
            SystemTray tray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit()
                    .createImage(getClass().getResource("/logo.png"));
            trayIcon = new TrayIcon(image, "GitHub Popup");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("Github Oops");
            tray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException();
        }


    }
    public  void setMenu(String login, list<RepositoryDescription> repos){
        PopupMenu popup = new PopupMenu();
        MenuItem accountMI = new MenuItem(login);
        accountMI.addActionListener(e->openInBrowser("https://github.com/"+login));


        MenuItem notificationMI = new MenuItem("notifications");
        notificationMI.addActionListener(e->openInBrowser("https://github.com/notifications"));

        Menu repositoriesMI = new Menu ("repositories");
        repos
                .forEach(repo ->{
                    String name = repo.getPrs().size()>0
                            ? String.format("(%d)%s", repo.getPrs().size()): repo.getName();
                    repo.getName();

            new Menu(repo.getName());
            Menu repoSM = new Menu(name);
            repositoriesMI.add(repoSM);
                });

        popup.add(accountMI);
        popup.addSeparator();
        popup.add(notificationMI);


        trayIcon.setPopupMenu(popup);

    }
    public void openInBrowser(String url) {
        Desktop desktop = Desktop.getDesktop();

            try {
                desktop.browse((new URL(url).toURI()));

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void showNotification(String title, String text){
trayIcon.displayMessage(title, text, TrayIcon.MessageType.INFO);
    }

    private class list<T> {
    }
}
