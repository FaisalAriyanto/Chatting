package chatting.faisal.pkp.chatting.Function;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionManager {
    private Context context;
    private ConnectivityManager cm;
    private NetworkInfo activeNetwork;

    public ConnectionManager(Context ctx) {
        context = ctx;
        cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();
    }

    public boolean isConnect() {
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public String connectionName() {
        if (activeNetwork != null) {
            return activeNetwork.getTypeName();
        }
        return null;
    }
}
