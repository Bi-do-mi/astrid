package com.bidomi.astrid.Converters;

import com.bidomi.astrid.Model.User;
import com.fasterxml.jackson.databind.util.StdConverter;

public class UserToUserId extends StdConverter<User, Long> {
    @Override
    public Long convert(User user) {
        return user.getId();
    }
}
