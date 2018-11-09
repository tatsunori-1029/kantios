package app.saiou.kantios.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("javadoc")
public class TbMemo implements Serializable {
    private String _memoId;

    private String _title;

    private String _createUserId;

    private Date _createTimestamp;

    private String _updateUserId;

    private Date _updateTimestamp;

    private String _content;

    private static final long serialVersionUID = 1L;

    public String getMemoId() {
        return _memoId;
    }

    public void setMemoId(String memoId) {
        _memoId = memoId;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getCreateUserId() {
        return _createUserId;
    }

    public void setCreateUserId(String createUserId) {
        _createUserId = createUserId;
    }

    public Date getCreateTimestamp() {
        return _createTimestamp;
    }

    public void setCreateTimestamp(Date createTimestamp) {
        _createTimestamp = createTimestamp;
    }

    public String getUpdateUserId() {
        return _updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        _updateUserId = updateUserId;
    }

    public Date getUpdateTimestamp() {
        return _updateTimestamp;
    }

    public void setUpdateTimestamp(Date updateTimestamp) {
        _updateTimestamp = updateTimestamp;
    }

    public String getContent() {
        return _content;
    }

    public void setContent(String content) {
        _content = content;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TbMemo other = (TbMemo) that;
        return (this.getMemoId() == null ? other.getMemoId() == null : this.getMemoId().equals(other.getMemoId()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getCreateUserId() == null ? other.getCreateUserId() == null : this.getCreateUserId().equals(other.getCreateUserId()))
            && (this.getCreateTimestamp() == null ? other.getCreateTimestamp() == null : this.getCreateTimestamp().equals(other.getCreateTimestamp()))
            && (this.getUpdateUserId() == null ? other.getUpdateUserId() == null : this.getUpdateUserId().equals(other.getUpdateUserId()))
            && (this.getUpdateTimestamp() == null ? other.getUpdateTimestamp() == null : this.getUpdateTimestamp().equals(other.getUpdateTimestamp()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMemoId() == null) ? 0 : getMemoId().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getCreateUserId() == null) ? 0 : getCreateUserId().hashCode());
        result = prime * result + ((getCreateTimestamp() == null) ? 0 : getCreateTimestamp().hashCode());
        result = prime * result + ((getUpdateUserId() == null) ? 0 : getUpdateUserId().hashCode());
        result = prime * result + ((getUpdateTimestamp() == null) ? 0 : getUpdateTimestamp().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        return result;
    }
}