package com.web.sukusuku.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attachment")
@Data
@NoArgsConstructor
@ToString(exclude = {"post"})
public class Attachment {

    @Id
    @Column(name = "attachment_id")
    private Integer attachmentId;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
