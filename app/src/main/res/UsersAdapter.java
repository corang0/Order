package com.example.listview1;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.CustomViewHolder> {
    private ArrayList<ProductData> mList = null;
    private Activity context = null;
    // Item의 클릭 상태를 저장할 array 객체
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;

    public interface OnItemClickListener {
        void onItemClick(View v, int pos, String count);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public UsersAdapter(Activity context, ArrayList<ProductData> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView id;
        protected TextView name;
        protected TextView price;
        protected Button button;
        protected Button buttonPlus;
        protected Button buttonMinus;
        protected TextView count;
        protected LinearLayout expandable;
        private ProductData data;
        private int position;

        public CustomViewHolder(View view) {
            super(view);
            this.id = (TextView) view.findViewById(R.id.textView_list_id);
            this.name = (TextView) view.findViewById(R.id.textView_list_name);
            this.price = (TextView) view.findViewById(R.id.textView_list_price);
            this.button = (Button) view.findViewById(R.id.button);
            this.expandable = (LinearLayout) view.findViewById(R.id.expandable);
            this.buttonPlus = (Button) view.findViewById(R.id.button_plus);
            this.buttonMinus = (Button) view.findViewById(R.id.button_minus);
            this.count = (TextView) view.findViewById(R.id.textView_count);
        }

        void onBind(ProductData data, int position) {
            this.data = data;
            this.position = position;

            this.id.setText(data.getMember_id());
            this.name.setText(data.getMember_name());
            this.price.setText(data.getMember_price());

            changeVisibility(selectedItems.get(position));

            itemView.setOnClickListener(this);
            this.button.setOnClickListener(this);
            this.buttonPlus.setOnClickListener(this);
            this.buttonMinus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.linearItem:
                    if (selectedItems.get(position)) {
                        // 펼쳐진 Item을 클릭 시
                        selectedItems.delete(position);
                    } else {
                        // 직전의 클릭됐던 Item의 클릭상태를 지움
                        selectedItems.delete(prePosition);
                        // 클릭한 Item의 position을 저장
                        selectedItems.put(position, true);
                    }
                    // 해당 포지션의 변화를 알림
                    if (prePosition != -1) notifyItemChanged(prePosition);
                    notifyItemChanged(position);
                    // 클릭된 position 저장
                    prePosition = position;
                    break;
                case R.id.button:
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos, count.getText().toString());
                        }
                    }
                    break;
                case R.id.button_plus:
                    this.count.setText(Integer.toString(Integer.parseInt(this.count.getText().toString()) + 1));
                    break;
                case R.id.button_minus:
                    this.count.setText(Integer.toString(Integer.parseInt(this.count.getText().toString()) - 1));
                    break;
            }
        }

        private void changeVisibility(final boolean isExpanded) {
            /*// height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 150;
            float d = context.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // value는 height 값
                    int value = (int) animation.getAnimatedValue();
                    // imageView의 높이 변경
                    button.getLayoutParams().height = value;
                    button.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    button.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            // Animation start
            va.start();*/

            expandable.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        viewholder.onBind(mList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
