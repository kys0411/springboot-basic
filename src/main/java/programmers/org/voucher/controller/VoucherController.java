package programmers.org.voucher.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import programmers.org.voucher.constant.Command;
import programmers.org.voucher.constant.VoucherType;
import programmers.org.voucher.domain.Voucher;
import programmers.org.voucher.io.Input;
import programmers.org.voucher.io.Output;
import programmers.org.voucher.io.VoucherConsole;
import programmers.org.voucher.service.VoucherService;

import java.util.List;
import java.util.NoSuchElementException;

import static programmers.org.voucher.exception.ErrorMessage.*;

@Component
public class VoucherController implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(VoucherController.class);

    private final VoucherService voucherService;
    private final VoucherConsole voucherConsole;

    public VoucherController(VoucherService voucherService) {
        this.voucherService = voucherService;
        this.voucherConsole = new VoucherConsole(new Input(), new Output());
    }

    @Override
    public void run(String... args) {
        boolean isRunning = true;

        while (isRunning) {
            voucherConsole.printManual();
            String inputCommand = voucherConsole.inputCommand();

            Command command = Command.find(inputCommand)
                    .orElseThrow(() -> new NoSuchElementException(COMMAND_ERROR_MESSAGE.getMessage()));

            switch (command) {
                case CREATE:
                    createVoucher();
                    break;
                case LIST:
                    printVoucherList();
                    break;
                case EXIT:
                    isRunning = false;
            }
        }
    }

    private void createVoucher() {
        String voucherType = voucherConsole.inputVoucherType();

        VoucherType findVoucherType = VoucherType.find(voucherType)
                .orElseThrow(() -> new NoSuchElementException(VOUCHER_ERROR_MESSAGE.getMessage()));

        int voucherInfo = voucherConsole.inputVoucherInfo();
        voucherService.create(voucherInfo, findVoucherType);
    }

    private void printVoucherList() {
        List<Voucher> voucherList = voucherService.getAllVouchers();
        voucherConsole.printVoucherList(voucherList);
    }
}
