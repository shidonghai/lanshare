
package com.nano.lanshare.history.logic;

public interface IHistoryDelete {
    void prepareDelete();

    void startDelete();

    void deleting();

    void cancelDelete();

    void finishDeleted();
}
