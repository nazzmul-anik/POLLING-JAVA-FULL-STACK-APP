package com.nazmul.polling.services.user;

import com.nazmul.polling.repository.OptionsRepository;
import com.nazmul.polling.repository.PollRepository;
import com.nazmul.polling.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PollServiceImpl implements PollService {

    private final JwtUtil jwtUtil;
    private final PollRepository pollRepository;
    private final OptionsRepository optionsRepository;
}
