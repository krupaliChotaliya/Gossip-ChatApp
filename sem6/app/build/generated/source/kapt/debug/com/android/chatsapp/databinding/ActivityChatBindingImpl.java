package com.android.chatsapp.databinding;
import com.android.chatsapp.R;
import com.android.chatsapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityChatBindingImpl extends ActivityChatBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.toolbar, 2);
        sViewsWithIds.put(R.id.profileImg, 3);
        sViewsWithIds.put(R.id.username, 4);
        sViewsWithIds.put(R.id.block, 5);
        sViewsWithIds.put(R.id.messageRecycleView, 6);
        sViewsWithIds.put(R.id.chatlayout, 7);
        sViewsWithIds.put(R.id.attachbtn, 8);
        sViewsWithIds.put(R.id.messageBox, 9);
        sViewsWithIds.put(R.id.sendBtn, 10);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers
    private androidx.databinding.InverseBindingListener statusandroidTextAttrChanged = new androidx.databinding.InverseBindingListener() {
        @Override
        public void onChange() {
            // Inverse of user.status.get()
            //         is user.status.set((java.lang.String) callbackArg_0)
            java.lang.String callbackArg_0 = androidx.databinding.adapters.TextViewBindingAdapter.getTextString(status);
            // localize variables for thread safety
            // user != null
            boolean userJavaLangObjectNull = false;
            // user
            com.android.chatsapp.viewmodel.UserViewModel user = mUser;
            // user.status != null
            boolean userStatusJavaLangObjectNull = false;
            // user.status.get()
            java.lang.String userStatusGet = null;
            // user.status
            androidx.databinding.ObservableField<java.lang.String> userStatus = null;



            userJavaLangObjectNull = (user) != (null);
            if (userJavaLangObjectNull) {


                userStatus = user.getStatus();

                userStatusJavaLangObjectNull = (userStatus) != (null);
                if (userStatusJavaLangObjectNull) {




                    userStatus.set(((java.lang.String) (callbackArg_0)));
                }
            }
        }
    };

    public ActivityChatBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds));
    }
    private ActivityChatBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (android.widget.ImageButton) bindings[8]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.LinearLayout) bindings[7]
            , (android.widget.EditText) bindings[9]
            , (androidx.recyclerview.widget.RecyclerView) bindings[6]
            , (de.hdodenhof.circleimageview.CircleImageView) bindings[3]
            , (android.widget.ImageButton) bindings[10]
            , (android.widget.TextView) bindings[1]
            , (androidx.appcompat.widget.Toolbar) bindings[2]
            , (android.widget.TextView) bindings[4]
            );
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.status.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.user == variableId) {
            setUser((com.android.chatsapp.viewmodel.UserViewModel) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setUser(@Nullable com.android.chatsapp.viewmodel.UserViewModel User) {
        this.mUser = User;
        synchronized(this) {
            mDirtyFlags |= 0x2L;
        }
        notifyPropertyChanged(BR.user);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeUserStatus((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeUserStatus(androidx.databinding.ObservableField<java.lang.String> UserStatus, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        com.android.chatsapp.viewmodel.UserViewModel user = mUser;
        java.lang.String userStatusGet = null;
        androidx.databinding.ObservableField<java.lang.String> userStatus = null;

        if ((dirtyFlags & 0x7L) != 0) {



                if (user != null) {
                    // read user.status
                    userStatus = user.getStatus();
                }
                updateRegistration(0, userStatus);


                if (userStatus != null) {
                    // read user.status.get()
                    userStatusGet = userStatus.get();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x7L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.status, userStatusGet);
        }
        if ((dirtyFlags & 0x4L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setTextWatcher(this.status, (androidx.databinding.adapters.TextViewBindingAdapter.BeforeTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.OnTextChanged)null, (androidx.databinding.adapters.TextViewBindingAdapter.AfterTextChanged)null, statusandroidTextAttrChanged);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): user.status
        flag 1 (0x2L): user
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}