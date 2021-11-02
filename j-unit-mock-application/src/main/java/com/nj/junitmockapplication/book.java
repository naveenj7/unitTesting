package com.nj.junitmockapplication;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "book_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookId;

    @NonNull
    private String bookName;

    private int rating;


}
