package com.example.alvaro.client_audit.activities.actionActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import com.example.alvaro.client_audit.R;
import com.example.alvaro.client_audit.activities.AsyncTaskActivity;
import com.example.alvaro.client_audit.controllers.adapters.NodeTreeViewAdapter;
import com.example.alvaro.client_audit.core.networks.Connection;
import com.github.ybq.android.spinkit.SpinKitView;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class HardwareActivity extends AsyncTaskActivity {

    private TreeNode root;
    private List<TreeNode> nodes;
    private AndroidTreeView tView;
    private JSONObject response;
    private RelativeLayout layout;
    private SpinKitView loader;
    private boolean onExecute;

    /*
        On create
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware);
        this.onExecute = false;
        loader = (SpinKitView) findViewById(R.id.hardware_anim_load);

        this.layout = (RelativeLayout) findViewById(R.id.hardware_activity_layout);
        tView = new AndroidTreeView(this, TreeNode.root());
        this.start_animation();

        JSONObject query = new JSONObject();
        try {
            query.put("command","HWinfo");
            Connection.get_connection().execute_command(query, this);
        } catch (JSONException e) {
            Log.e("hardware", Arrays.toString(e.getStackTrace()));
        }

    }

    /*
        start animation while asyncTask
     */
    @Override
    public void start_animation() {
        this.onExecute = true;
        loader.setVisibility(View.VISIBLE);
        loader.setIndeterminateDrawable(this.w);
    }

    /*
        called by asyncTask when it terminates
     */

    @Override
    public void stop_animation(Object... objects) {
        response = (JSONObject) objects[0];
        try {
            nodes = this.getNodes(response.get("data"),0);
        } catch (JSONException e) {
            nodes = new ArrayList<>();
        }
        root = TreeNode.root();
        root.addChildren(nodes);
        tView = new AndroidTreeView(this, root);
        tView.setDefaultAnimation(true);
        this.layout.addView(tView.getView());
        loader.setVisibility(View.GONE);
        this.onExecute = false;
    }

    /*
        Construct TreeView
            * recursive method
     */

    public List<TreeNode> getNodes(Object data_json, int level) throws JSONException {
        Log.d("JSON",data_json.toString());
        List<TreeNode> nodes = new ArrayList<>();
        if(data_json instanceof JSONArray){
            JSONArray data_list = (JSONArray) data_json;
            for(int i = 0; i < data_list.length(); i++){
                if(data_list.get(i).getClass().isPrimitive() || data_list.get(i) instanceof String){
                    String value = data_list.get(i).toString();
                    NodeTreeViewAdapter.NodeItem node =
                            new NodeTreeViewAdapter.NodeItem(value,"", get_image(value),level);
                    TreeNode new_node = new TreeNode(node).setViewHolder(new NodeTreeViewAdapter(this.getApplicationContext()));
                    nodes.add(new_node);
                }else{
                    nodes.addAll(getNodes(data_list.get(i),level));
                }
            }
        }else{
            JSONObject data = (JSONObject) data_json;
            Iterator<String> data_iter = data.keys();
            while(data_iter.hasNext()){
                String key = data_iter.next();
                String value = "";
                if(data.get(key).getClass().isPrimitive() || data.get(key) instanceof String){
                    value = data.get(key).toString();
                    NodeTreeViewAdapter.NodeItem node =
                            new NodeTreeViewAdapter.NodeItem(key,value, get_image(key),level);
                    TreeNode new_node = new TreeNode(node).setViewHolder(new NodeTreeViewAdapter(this.getApplicationContext()));
                    nodes.add(new_node);
                }else{
                    NodeTreeViewAdapter.NodeItem node =
                            new NodeTreeViewAdapter.NodeItem(key,value, get_image(key),level);
                    TreeNode new_node = new TreeNode(node).setViewHolder(new NodeTreeViewAdapter(this.getApplicationContext()));
                    new_node.addChildren(getNodes(data.get(key),level+1));
                    nodes.add(new_node);
                }
            }
        }

        return nodes;
    }

    /*
        get associated image
     */

    private int get_image(String str){
        int result;

        switch (str){
            case "battery": result = R.drawable.ic_battery;
                    break;
            case "cpu": result = R.drawable.ic_cpu;
                    break;
            case "disks": result = R.drawable.ic_disks;
                break;
            case "system": result = R.drawable.ic_system;
                break;
            case "virtual memory": result = R.drawable.ic_memory;
                break;
            default: result = R.drawable.ic_hardware_default;
                break;
        }

        return result;
    }

    @Override
    public void onBackPressed() {
        if (!onExecute) {
            super.onBackPressed();
        }
    }


}
