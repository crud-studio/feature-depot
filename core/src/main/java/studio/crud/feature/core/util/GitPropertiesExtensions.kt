package studio.crud.feature.core.util

import org.springframework.boot.info.GitProperties


val GitProperties.buildUserEmail get() = get("build.user.email")
val GitProperties.buildHost get() = get("build.host")
val GitProperties.dirty get() = get("dirty")
val GitProperties.remoteOriginUrl get() = get("remote.origin.url")
val GitProperties.closestTagName get() = get("closest.tag.name")
val GitProperties.commitIdDescribeShort get() = get("commit.id.describe-short")
val GitProperties.commitUserEmail get() = get("commit.user.email")
val GitProperties.commitTime get() = get("commit.time")
val GitProperties.commitMessageFull get() = get("commit.message.full")
val GitProperties.buildVersion get() = get("build.version")
val GitProperties.commitMessageShort get() = get("commit.message.short")
val GitProperties.commitIdAbbrev get() = get("commit.id.abbrev")
val GitProperties.branch get() = get("branch")
val GitProperties.buildUserName get() = get("build.user.name")
val GitProperties.closestTagCommitCount get() = get("closest.tag.commit.count")
val GitProperties.commitIdDescribe get() = get("commit.id.describe")
val GitProperties.commitId get() = get("commit.id")
val GitProperties.tags get() = get("tags")
val GitProperties.buildTime get() = get("build.time")
val GitProperties.commitUserName get() = get("commit.user.name")
