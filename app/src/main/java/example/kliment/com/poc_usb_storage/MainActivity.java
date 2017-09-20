package example.kliment.com.poc_usb_storage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

  private Button createFileBtn;
  private RecyclerView itemsRv;

  private SyncDelegate syncDelegate;

  private ItemsAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    createFileBtn = (Button) findViewById(R.id.create_file_btn);
    itemsRv = (RecyclerView) findViewById(R.id.items_rv);

    syncDelegate = new SyncDelegate(this);

    createFileBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FileUtils.createRandomFile(MainActivity.this);
//        Toast.makeText(MainActivity.this,getFilesDir().toString(),Toast.LENGTH_LONG).show();
        adapter.updateData(FileUtils.getFiles(MainActivity.this));
        adapter.notifyDataSetChanged();
      }
    });

    adapter = new ItemsAdapter(FileUtils.getFiles(this), syncDelegate);

    itemsRv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    itemsRv.setAdapter(adapter);

  }



}
