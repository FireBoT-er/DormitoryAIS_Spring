package com.cws._7s_cr.Miscellaneous;

import com.cws._7s_cr.Models.Rooms;
import com.cws._7s_cr.Models.Students;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StudentsToCEView {
    private Students student;
    private Rooms room;
}
