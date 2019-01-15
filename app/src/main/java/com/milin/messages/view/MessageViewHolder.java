package com.milin.messages.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milin.messages.R;
import com.milin.messages.model.CompleteMessage;
import com.milin.messages.model.pojo.Attachment;
import com.milin.messages.model.pojo.User;
import com.milin.messages.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MessageViewHolder extends RecyclerView.ViewHolder {
    private TextView content;
    private TextView name;
    private ImageView avatar;
    private LinearLayout attachmentsContainer;
    private AttachmentLongPressListener attachmentLongPressListener;

    MessageViewHolder(View itemView, AttachmentLongPressListener attachmentLongPressListener) {
        super(itemView);
        content = itemView.findViewById(R.id.content);
        name = itemView.findViewById(R.id.userName);
        avatar = itemView.findViewById(R.id.avatar);
        attachmentsContainer = itemView.findViewById(R.id.attachmentContainer);
        this.attachmentLongPressListener = attachmentLongPressListener;
    }

    public void bindView(CompleteMessage completeMessage, int viewType) {
        if(completeMessage != null) {
            content.setText(completeMessage.message.content);
            if(viewType % 2 == MessagesAdapter.VIEW_TYPE_THIS) {
                name.setText(name.getContext().getResources().getText(R.string.message_me));
            } else {
                if (completeMessage.users != null && completeMessage.users.size() > 0) {
                    User user = completeMessage.users.get(0);
                    name.setText(user.name);
                    Picasso.get().load(user.avatarId)
                            .transform(new CircleTransform())
                            .into(avatar);
                }
            }
            if(completeMessage.attachments != null) {
                for (int i = 0; i < completeMessage.attachments.size(); i++) {
                    Attachment attachment = completeMessage.attachments.get(i);
                    CardView attachmentCard =  ((CardView) attachmentsContainer.getChildAt(i));
                    Picasso.get().load(attachment.thumbnailUrl)
                            .into((ImageView) attachmentCard.findViewById(R.id.attachmentImage));
                    ((TextView) attachmentCard.findViewById(R.id.attachmentText)).setText(attachment.title);
                    attachmentCard.setOnLongClickListener(view -> {
                        attachmentLongPressListener.onAttachmentLongClicked(attachment);
                        return true;
                    });
                }
            }
        }
    }

    public interface AttachmentLongPressListener {
        void onAttachmentLongClicked(Attachment attachment);
    }
}
