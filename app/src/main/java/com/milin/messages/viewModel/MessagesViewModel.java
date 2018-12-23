package com.milin.messages.viewModel;

import android.app.Application;

import com.milin.messages.model.CompleteMessage;
import com.milin.messages.model.MessagesModel;
import com.milin.messages.model.pojo.Attachment;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class MessagesViewModel extends AndroidViewModel {

    private static final int PAGE_SIZE = 20;

    private MessagesModel messagesModel;

    public MessagesViewModel(Application application) {
        super(application);
        messagesModel = new MessagesModel(application);
        messagesModel.fetchMessages(application);
    }

    public LiveData<PagedList<CompleteMessage>> getPagedMessagesList() {
        return new LivePagedListBuilder<>(messagesModel.getMessagesDataSource(), getPageConfig())
                .build();
    }

    public void addMessage(CompleteMessage completeMessage) {
        messagesModel.addMessage(completeMessage.message);
    }

    public void removeMessage(CompleteMessage completeMessage) {
        messagesModel.removeMessage(completeMessage.message);
    }

    public void removeAttachment(Attachment attachment) {
        messagesModel.removeAttachment(attachment);
    }

    public void addAttachment(Attachment attachment) {
        messagesModel.addAttachment(attachment);
    }

    private static PagedList.Config getPageConfig() {
        return new PagedList.Config.Builder().setEnablePlaceholders(true).setPageSize(PAGE_SIZE).build();
    }
}
