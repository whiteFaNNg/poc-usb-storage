package example.kliment.com.poc_usb_storage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kliment on 9/20/2017.
 */

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.Holder>{

  private List<String> files;

  private SyncDelegate syncDelegate;

  public ItemsAdapter(List<String> files, SyncDelegate syncDelegate){
    this.files = files;
    this.syncDelegate = syncDelegate;
  }

  public void updateData(List<String> files){
    this.files = files;
  }

  @Override
  public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    // Inflate the custom layout
    View item = inflater.inflate(R.layout.item, parent, false);

    // Return a new holder instance
    return new ItemsAdapter.Holder(item);
  }

  @Override
  public void onBindViewHolder(Holder holder, int position) {
    holder.filename.setText(files.get(position));
    holder.status.setText("FALSE");
  }

  @Override
  public int getItemCount() {
    return files.size();
  }

  class Holder extends RecyclerView.ViewHolder{
    public TextView filename;
    public TextView status;
    public Button sync;

    public Holder(final View itemView) {
      super(itemView);
      filename = (TextView) itemView.findViewById(R.id.filenameTv);
      status = (TextView) itemView.findViewById(R.id.statusTv);
      sync = (Button) itemView.findViewById(R.id.syncBtn);

      sync.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          syncDelegate.syncFile(files.get(getAdapterPosition()));
        }
      });
    }
  }

}
