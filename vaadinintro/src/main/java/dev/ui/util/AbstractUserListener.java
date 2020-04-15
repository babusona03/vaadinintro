package dev.ui.util;

import java.time.LocalDateTime;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import dev.ui.entity.AbstractUser;

public class AbstractUserListener {
	@PrePersist
	public void setCreationStamp(AbstractUser abstractUser ) {
		abstractUser.setCreatedOn(LocalDateTime.now());
	}
	
	@PreUpdate
	public void setUpdateStamp(AbstractUser abstractUser) {
		abstractUser.setUpdatedOn(LocalDateTime.now());
	}
}
