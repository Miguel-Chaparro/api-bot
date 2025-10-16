package com.dom.ws.rest.bot.DTO;

import java.util.List;

public class UsersPageDTO {
    private List<UserDTO> users;
    private int page;
    private int pageSize;
    private long total;
    private boolean hasMore;

    public UsersPageDTO() {}

    public List<UserDTO> getUsers() { return users; }
    public void setUsers(List<UserDTO> users) { this.users = users; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public boolean isHasMore() { return hasMore; }
    public void setHasMore(boolean hasMore) { this.hasMore = hasMore; }
}
