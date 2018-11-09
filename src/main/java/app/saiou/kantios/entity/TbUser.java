package app.saiou.kantios.entity;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("javadoc")
public class TbUser implements Serializable {
    private String _userId;

    private String _authUserId;

    private String _displayName;

    private String _createUserId;

    private Date _createTimestamp;

    private String _updateUserId;

    private Date _updateTimestamp;

    private static final long serialVersionUID = 1L;

    public String getUserId() {
        return _userId;
    }

    public void setUserId(String userId) {
        _userId = userId;
    }

    public String getAuthUserId() {
        return _authUserId;
    }

    public void setAuthUserId(String authUserId) {
        _authUserId = authUserId;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public void setDisplayName(String displayName) {
        _displayName = displayName;
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
        TbUser other = (TbUser) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getAuthUserId() == null ? other.getAuthUserId() == null : this.getAuthUserId().equals(other.getAuthUserId()))
            && (this.getDisplayName() == null ? other.getDisplayName() == null : this.getDisplayName().equals(other.getDisplayName()))
            && (this.getCreateUserId() == null ? other.getCreateUserId() == null : this.getCreateUserId().equals(other.getCreateUserId()))
            && (this.getCreateTimestamp() == null ? other.getCreateTimestamp() == null : this.getCreateTimestamp().equals(other.getCreateTimestamp()))
            && (this.getUpdateUserId() == null ? other.getUpdateUserId() == null : this.getUpdateUserId().equals(other.getUpdateUserId()))
            && (this.getUpdateTimestamp() == null ? other.getUpdateTimestamp() == null : this.getUpdateTimestamp().equals(other.getUpdateTimestamp()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getAuthUserId() == null) ? 0 : getAuthUserId().hashCode());
        result = prime * result + ((getDisplayName() == null) ? 0 : getDisplayName().hashCode());
        result = prime * result + ((getCreateUserId() == null) ? 0 : getCreateUserId().hashCode());
        result = prime * result + ((getCreateTimestamp() == null) ? 0 : getCreateTimestamp().hashCode());
        result = prime * result + ((getUpdateUserId() == null) ? 0 : getUpdateUserId().hashCode());
        result = prime * result + ((getUpdateTimestamp() == null) ? 0 : getUpdateTimestamp().hashCode());
        return result;
    }
}