// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;


public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

        if (request.getAttendees().size() == 0){ 
            return Arrays.asList(TimeRange.WHOLE_DAY);
        }
        if (request.getDuration() > TimeRange.WHOLE_DAY.duration()){ 
            return Arrays.asList();
        }

        Set<TimeRange> timesSet = new HashSet<>(); 
        Set<String> attendees = new HashSet<>(request.getAttendees());
        for (Event e : events){
            for (String attendee : e.getAttendees()){ 
                if (attendees.contains(attendee)) //O(1) 
                    timesSet.add(e.getWhen()); //O(e * att) where e = # of events & att = average # of attendees
            }
        }

        List<TimeRange> times = new ArrayList<>(timesSet);
        Collections.sort(times, TimeRange.ORDER_BY_START); //O(nlogn) where n = # of (relevant) events
        if (times.size() > 1){
            ListIterator<TimeRange> iter = times.listIterator(1);
            while (iter.hasNext()){ //O(n) where n = # of (relevant) events
                TimeRange prev = iter.previous();
                iter.next();
                TimeRange curr = iter.next();
                if (prev.overlaps(curr)){
                    iter.previous();
                    int st1 = iter.previous().start(), end1 = iter.next().end();
                    iter.remove();
                    int st2 = iter.next().start(), end2 = iter.previous().end();
                    iter.remove();
                    TimeRange combined = TimeRange.fromStartEnd(st1 < st2 ? st1 : st2, end1 > end2 ? end1 : end2, false);
                    iter.add(combined);
                }
            }
        }

        List<TimeRange> options = new ArrayList<>();
        int start = TimeRange.START_OF_DAY;
        for (int i = 0; i < times.size(); i++){ //O(n) where n = # of (relevant) events
            if (start != times.get(i).start())
                options.add(TimeRange.fromStartEnd(start, times.get(i).start(), false));
            start = times.get(i).end();
        }
        if (start < TimeRange.END_OF_DAY){
            options.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, true));
        }
        return options;
    }
}
