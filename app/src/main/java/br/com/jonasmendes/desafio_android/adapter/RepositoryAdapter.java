package br.com.jonasmendes.desafio_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import br.com.jonasmendes.desafio_android.R;
import br.com.jonasmendes.desafio_android.domain.Repository;
import br.com.jonasmendes.desafio_android.utils.MyApplication;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.ViewHolder>{
    List<Repository> repositoryList = new ArrayList<>();

    public RepositoryAdapter(List<Repository> repositoryList) {
        this.repositoryList = repositoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View repositoryView = inflater.inflate(R.layout.item_repository, parent, false);

        ViewHolder viewHolder = new ViewHolder(repositoryView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ImageLoader imageLoader = ImageLoader.getInstance();

        Repository repository = repositoryList.get(position);

        TextView tvRepositoryTitle = holder.tvRepositoryTitle;
        tvRepositoryTitle.setText(repository.getName());

        TextView tvRepositoryDescr = holder.tvRepositoryDescr;
        tvRepositoryDescr.setText(repository.getDescription());

        TextView tvRepositoryUsername = holder.tvRepositoryUsername;
        tvRepositoryUsername.setText(repository.getOwner().getLogin());

        TextView tvRepositoryFullname = holder.tvRepositoryFullname;
        tvRepositoryFullname.setText(repository.getOwner().getLogin());

        TextView tvRepositoryQtdFork = holder.tvRepositoryQtdFork;
        tvRepositoryQtdFork.setText("{faw-code-fork} " + repository.getForks_count());

        TextView tvRepositoryQtdStar = holder.tvRepositoryQtdStar;
        tvRepositoryQtdStar.setText("{faw-star} " + repository.getStargazers_count());

        ImageView civRepositoryUser = holder.civRepositoryUser;

        imageLoader.displayImage(repository.getOwner().getAvatar_url(), civRepositoryUser);

    }

    @Override
    public int getItemCount() {
        return repositoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRepositoryTitle,tvRepositoryDescr,tvRepositoryUsername,tvRepositoryFullname,tvRepositoryQtdFork,tvRepositoryQtdStar;
        public ImageView civRepositoryUser;
        public ViewHolder(View itemView) {
            super(itemView);
            tvRepositoryTitle = (TextView) itemView.findViewById(R.id.tvRepositoryTitle);
            tvRepositoryDescr = (TextView) itemView.findViewById(R.id.tvRepositoryDescr);
            tvRepositoryUsername = (TextView) itemView.findViewById(R.id.tvRepositoryUsername);
            tvRepositoryFullname = (TextView) itemView.findViewById(R.id.tvRepositoryFullname);
            tvRepositoryQtdFork = (TextView) itemView.findViewById(R.id.tvRepositoryQtdFork);
            tvRepositoryQtdStar = (TextView) itemView.findViewById(R.id.tvRepositoryQtdStar);
            civRepositoryUser = (ImageView)itemView.findViewById(R.id.civRepositoryUser);
        }
    }
}
