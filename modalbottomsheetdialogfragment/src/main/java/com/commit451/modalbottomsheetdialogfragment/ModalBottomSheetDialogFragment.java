package com.commit451.modalbottomsheetdialogfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * [BottomSheetDialogFragment] which can show a selection of options. Create using the [Builder]
 */
public class ModalBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private static String KEY_OPTIONS = "options";
    private static String KEY_LAYOUT = "layout";
    private static String KEY_COLUMNS = "columns";
    private static String KEY_HEADER = "header";
    private static String KEY_HEADER_LAYOUT_RES = "header_layout_res";

    private static ModalBottomSheetDialogFragment newInstance(Builder builder) {
        ModalBottomSheetDialogFragment fragment = new ModalBottomSheetDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_OPTIONS, builder.options);
        args.putInt(KEY_LAYOUT, builder.layoutRes);
        args.putInt(KEY_COLUMNS, builder.columns);
        args.putString(KEY_HEADER, builder.header);
        args.putInt(KEY_HEADER_LAYOUT_RES, builder.headerLayoutRes);
        fragment.setArguments(args);
        return fragment;
    }

    private ModalBottomSheetDialogFragment.Adapter adapter;
    private ModalBottomSheetDialogFragment.Listener listener;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.modal_bottom_sheet_dialog_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView list = view.findViewById(R.id.list);
        Bundle arguments = getArguments();
        if (arguments == null) {
            throw new IllegalStateException("You need to create this via the builder");
        }
        ArrayList<OptionHolder> optionHolders = arguments.getParcelableArrayList(KEY_OPTIONS);
        ArrayList<Option> options = new ArrayList<>();

        if (optionHolders != null) {
            for (OptionHolder it : optionHolders) {
                Integer resource = it.resource;
                OptionRequest optionRequest = it.optionRequest;
                if (resource != null) {
                    inflate(resource, options);
                }
                if (optionRequest != null) {
                    options.add(optionRequest.toOption(getContext()));
                }
            }
        }

        adapter = new Adapter(new Callback() {
            @Override
            public void invoke(Option option) {
                listener.onModalOptionSelected(ModalBottomSheetDialogFragment.this.getTag(), option);
                dismissAllowingStateLoss();
            }
        });
        adapter.layoutRes = arguments.getInt(KEY_LAYOUT);
        adapter.header = arguments.getString(KEY_HEADER);
        adapter.headerLayoutRes = arguments.getInt(KEY_HEADER_LAYOUT_RES);

        list.setAdapter(adapter);
        final int columns = arguments.getInt(KEY_COLUMNS);
        if (columns == 1) {
            list.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), columns);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (adapter.header != null && position == 0) ? columns : 1;
                }
            });
            list.setLayoutManager(layoutManager);
        }

        adapter.set(options);
        listener = bindHost();
    }

    @SuppressLint("RestrictedApi")
    private void inflate(int menuRes, List<Option> options) {
        MenuBuilder menu = new MenuBuilder(getContext());
        new MenuInflater(getContext()).inflate(menuRes, menu);
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            Option option = new Option(item.getItemId(), item.getTitle(), item.getIcon());
            options.add(option);
        }
    }

    private Listener bindHost() {
        if (getParentFragment() != null) {
            if (getParentFragment() instanceof Listener) {
                return (Listener) getParentFragment();
            }
        }
        if (getContext() instanceof Listener) {
            return (Listener) getContext();
        }
        throw new IllegalStateException("ModalBottomSheetDialogFragment must be attached to a parent (activity or fragment) that implements the ModalBottomSheetDialogFragment.Listener");
    }

    /**
     * Used to build a [ModalBottomSheetDialogFragment]
     */
    public static class Builder {
        ArrayList<OptionHolder> options = new ArrayList<>();
        @LayoutRes
        int layoutRes = R.layout.modal_bottom_sheet_dialog_fragment_item;
        private int columns = 1;
        private String header = null;
        private int headerLayoutRes = R.layout.modal_bottom_sheet_dialog_fragment_header;

        /**
         * Inflate the given menu resource to the options
         */
        public Builder add(@MenuRes int menuRes) {
            options.add(new OptionHolder(menuRes, null));
            return this;
        }

        /**
         * Add an option to the sheet
         */
        public Builder add(OptionRequest option) {
            options.add(new OptionHolder(null, option));
            return this;
        }

        /**
         * Set the custom layout resource to inflate for each option. Note that you need to have a
         * TextView with a resource id of @android:id/text1 if your option has a title and an ImageView
         * with a resource id of @android:id/icon if your option has a drawable associated
         */
        public Builder layout(@LayoutRes int layoutRes) {
            this.layoutRes = layoutRes;
            return this;
        }

        /**
         * Set the number of columns you want for your options
         */
        public Builder columns(int columns) {
            this.columns = columns;
            return this;
        }

        /**
         * Add a custom header to the modal, using the custom layout if provided
         */
        public Builder header(String header, @LayoutRes int layoutRes) {
            this.header = header;
            this.headerLayoutRes = layoutRes;
            return this;
        }

        public Builder header(String header) {
            return header(header, R.layout.modal_bottom_sheet_dialog_fragment_header);
        }
        /**
         * Build the [ModalBottomSheetDialogFragment]. You still need to call [ModalBottomSheetDialogFragment.show] when you want it to show
         */
        public ModalBottomSheetDialogFragment build() {
            return newInstance(this);
        }

        /**
         * Build and show the [ModalBottomSheetDialogFragment]
         */

        public final ModalBottomSheetDialogFragment show(FragmentManager fragmentManager, String tag) {
            ModalBottomSheetDialogFragment dialog = this.build();
            dialog.show(fragmentManager, tag);
            return dialog;
        }
    }

    /**
     * Listener for when the modal options are selected
     */
    public interface Listener {
        /**
         * A modal option has been selected
         */
        void onModalOptionSelected(String tag, Option option);
    }

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int VIEW_TYPE_HEADER = 0;
        public static final int VIEW_TYPE_ITEM = 1;

        private List<Option> options = new ArrayList<>();
        private int layoutRes = R.layout.modal_bottom_sheet_dialog_fragment_item;
        private int headerLayoutRes = R.layout.modal_bottom_sheet_dialog_fragment_header;

        private String header = null;
        private final Callback callback;

        private Adapter(Callback callback) {
            this.callback = callback;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    View view = LayoutInflater.from(parent.getContext()).inflate(headerLayoutRes, parent, false);
                    return new HeaderViewHolder(view);
                case VIEW_TYPE_ITEM:
                    view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
                    final ItemViewHolder holder = new ItemViewHolder(view);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int position = (header != null) ?
                                    holder.getAdapterPosition() - 1 :
                                    holder.getAdapterPosition();
                            Option option = options.get(position);
                            callback.invoke(option);
                        }
                    });
                    return holder;
            }
            throw new IllegalStateException("Wht is this");
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int correctedPosition = (header == null) ? position : position - 1;
            if (holder instanceof ItemViewHolder) {
                Option option = options.get(correctedPosition);
                ((ItemViewHolder) holder).bind(option);
            } else if (holder instanceof HeaderViewHolder) {
                ((HeaderViewHolder) holder).bind(header);
            }
        }

        @Override
        public int getItemCount() {
            return (header == null) ? options.size() : options.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (header != null) {
                if (position == 0) {
                    return VIEW_TYPE_HEADER;
                }
            }
            return VIEW_TYPE_ITEM;
        }

        void set(List<Option> options) {
            this.options.clear();
            this.options.addAll(options);
            notifyDataSetChanged();
        }
    }

    public interface Callback {
        void invoke(Option option);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView text;
        private final ImageView icon;

        public ItemViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(android.R.id.text1);
            icon = itemView.findViewById(android.R.id.icon);
        }

        void bind(Option option) {
            text.setText(option.title);
            icon.setImageDrawable(option.icon);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView text;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(android.R.id.text1);
        }

        void bind(String header) {
            text.setText(header);
        }
    }
}
