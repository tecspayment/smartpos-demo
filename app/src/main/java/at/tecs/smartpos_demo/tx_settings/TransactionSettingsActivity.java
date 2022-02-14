package at.tecs.smartpos_demo.tx_settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;

public class TransactionSettingsActivity extends AppCompatActivity {

    public static final String TX_EXTRA = "TX_EXTRA";
    public static final String MODE_EXTRA = "MODE_EXTRA";

    private TransactionEntity transaction;

    private TextInputEditText nameEdit;
    private TextInputEditText messageTypeEdit;
    private TextInputEditText amountEditText;
    private TextInputEditText currencyEditText;
    private TextInputEditText transSourceIDEdit;
    private TextInputEditText cardNumEditText;
    private TextInputEditText cvc2EditText;
    private TextInputEditText receiptNumEditText;
    private TextInputEditText paymentReasonEditText;
    private TextInputEditText transPlaceEditText;
    private TextInputEditText authNumberEditText;
    private TextInputEditText originIndicatorEditText;
    private TextInputEditText passwordEditText;
    private TextInputEditText ecrDataEditText;
    private TextInputEditText languageCodeEditText;
    private TextInputEditText receiptLayoutEditText;
    private TextInputEditText destinationCurrencyEditText;
    private TextInputEditText txOriginEditText;
    private TextInputEditText personalIDEditText;

    private ImageButton amountVisibilityButton;
    private ImageButton currencyVisibilityButton;
    private ImageButton sourceIDVisibilityButton;
    private ImageButton cardNumVisibilityButton;
    private ImageButton cvc2VisibilityButton;
    private ImageButton receiptNumVisibilityButton;
    private ImageButton reasonVisibilityButton;
    private ImageButton transPlaceVisibilityButton;
    private ImageButton authorNumVisibilityButton;
    private ImageButton origIndVisibilityButton;
    private ImageButton passwordVisibilityButton;
    private ImageButton ecrDataVisibilityButton;
    private ImageButton langCodeVisibilityButton;
    private ImageButton receiptLayoutVisibilityButton;
    private ImageButton destCurrencyVisibilityButton;
    private ImageButton txOriginVisibilityButton;
    private ImageButton personalIDVisibilityButton;

    private boolean edit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_settings_act);

        Intent intent = getIntent();

        boolean modify = intent.getBooleanExtra(MODE_EXTRA, false);
        transaction = (TransactionEntity) intent.getSerializableExtra(TX_EXTRA);

        if(transaction == null) {
            edit = false;
            transaction = new TransactionEntity();
            transaction.amountVisibility = true;
            transaction.currencyVisibility = true;
            transaction.sourceIDVisibility = true;
            transaction.cardNumVisibility = true;
            transaction.cvc2Visibility = true;
            transaction.receiptNumVisibility = true;
            transaction.paymentReasonVisibility = true;
            transaction.transPlaceVisibility = true;
            transaction.authorNumVisibility = true;
            transaction.originIndVisibility = true;
            transaction.passwordVisibility = true;
            transaction.userdataVisibility = true;
            transaction.langCodeVisibility = true;
            transaction.receiptLayoutVisibility = true;
            transaction.desCurrencyVisibility = true;
            transaction.personalIDVisibility = true;
            transaction.txOriginVisibility = true;
        }

        Button deleteButton = findViewById(R.id.deleteImageButton);
        Button saveImageButton = findViewById(R.id.saveImageButton);
        Button cancelImageButton = findViewById(R.id.cancelImageButton);

        if(!modify) {
           deleteButton.setVisibility(View.GONE);
        }

        nameEdit = findViewById(R.id.nameEditText);
        messageTypeEdit = findViewById(R.id.messageTypeEditText);

        amountEditText = findViewById(R.id.amountEditText);
        currencyEditText = findViewById(R.id.currencyEditText);
        transSourceIDEdit = findViewById(R.id.transSourceIDEdit);
        cardNumEditText = findViewById(R.id.cardNumEditText);
        cvc2EditText = findViewById(R.id.cvc2EditText);
        receiptNumEditText = findViewById(R.id.receiptNumEditText);
        paymentReasonEditText = findViewById(R.id.paymentReasonEditText);
        transPlaceEditText = findViewById(R.id.transPlaceEditText);
        authNumberEditText = findViewById(R.id.authNumberEditText);
        originIndicatorEditText = findViewById(R.id.originIndicatorEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        ecrDataEditText = findViewById(R.id.ecrDataEditText);
        languageCodeEditText = findViewById(R.id.languageCodeEditText);
        receiptLayoutEditText = findViewById(R.id.receiptLayoutEditText);
        destinationCurrencyEditText = findViewById(R.id.destinationCurrencyEditText);
        txOriginEditText = findViewById(R.id.txOriginEditText);
        personalIDEditText = findViewById(R.id.personalIDEditText);

        amountVisibilityButton = findViewById(R.id.amountVisibilityButton);
        currencyVisibilityButton = findViewById(R.id.currencyVisibilityButton);
        sourceIDVisibilityButton = findViewById(R.id.sourceIDVisibilityButton);
        cardNumVisibilityButton = findViewById(R.id.cardNumVisibilityButton);
        cvc2VisibilityButton = findViewById(R.id.cvc2VisibilityButton);
        receiptNumVisibilityButton = findViewById(R.id.receiptNumVisibilityButton);
        reasonVisibilityButton = findViewById(R.id.reasonVisibilityButton);
        transPlaceVisibilityButton = findViewById(R.id.transPlaceVisibilityButton);
        authorNumVisibilityButton = findViewById(R.id.authorNumVisibilityButton);
        origIndVisibilityButton = findViewById(R.id.origIndVisibilityButton);
        passwordVisibilityButton = findViewById(R.id.passwordVisibilityButton);
        ecrDataVisibilityButton = findViewById(R.id.ecrDataVisibilityButton);
        langCodeVisibilityButton = findViewById(R.id.langCodeVisibilityButton);
        receiptLayoutVisibilityButton = findViewById(R.id.receiptLayoutVisibilityButton);
        destCurrencyVisibilityButton = findViewById(R.id.destCurrencyVisibilityButton);
        txOriginVisibilityButton = findViewById(R.id.txOriginVisibilityButton);
        personalIDVisibilityButton = findViewById(R.id.personalIDVisibilityButton);


        if(transaction.name != null) {
            nameEdit.setText(transaction.name);
        }

        if(transaction.msgType != null) {
            messageTypeEdit.setText(transaction.msgType);
        }

        if (transaction.amountVisibility == null || !transaction.amountVisibility) {
            amountVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            amountVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        amountVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.amountVisibility == null || !transaction.amountVisibility) {
                    transaction.amountVisibility = true;
                    amountVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.amountVisibility = false;
                    amountVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });


        if (transaction.currencyVisibility == null || !transaction.currencyVisibility) {
            currencyVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            currencyVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        currencyVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.currencyVisibility == null || !transaction.currencyVisibility) {
                    transaction.currencyVisibility = true;
                    currencyVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.currencyVisibility = false;
                    currencyVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.sourceIDVisibility == null || !transaction.sourceIDVisibility) {
            sourceIDVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            sourceIDVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        sourceIDVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.sourceIDVisibility == null || !transaction.sourceIDVisibility) {
                    transaction.sourceIDVisibility = true;
                    sourceIDVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.sourceIDVisibility = false;
                    sourceIDVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.cardNumVisibility == null || !transaction.cardNumVisibility) {
            cardNumVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            cardNumVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        cardNumVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.cardNumVisibility == null || !transaction.cardNumVisibility) {
                    transaction.cardNumVisibility = true;
                    cardNumVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.cardNumVisibility = false;
                    cardNumVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.cvc2Visibility == null || !transaction.cvc2Visibility) {
            cvc2VisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            cvc2VisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        cvc2VisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.cvc2Visibility == null || !transaction.cvc2Visibility) {
                    transaction.cvc2Visibility = true;
                    cvc2VisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.cvc2Visibility = false;
                    cvc2VisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.receiptNumVisibility == null || !transaction.receiptNumVisibility) {
            receiptNumVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            receiptNumVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        receiptNumVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.receiptNumVisibility == null || !transaction.receiptNumVisibility) {
                    transaction.receiptNumVisibility = true;
                    receiptNumVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.receiptNumVisibility = false;
                    receiptNumVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.paymentReasonVisibility == null || !transaction.paymentReasonVisibility) {
            reasonVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            reasonVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        reasonVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.paymentReasonVisibility == null || !transaction.paymentReasonVisibility) {
                    transaction.paymentReasonVisibility = true;
                    reasonVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.paymentReasonVisibility = false;
                    reasonVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.transPlaceVisibility == null || !transaction.transPlaceVisibility) {
            transPlaceVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            transPlaceVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        transPlaceVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.transPlaceVisibility == null || !transaction.transPlaceVisibility) {
                    transaction.transPlaceVisibility = true;
                    transPlaceVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.transPlaceVisibility = false;
                    transPlaceVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.authorNumVisibility == null || !transaction.authorNumVisibility) {
            authorNumVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            authorNumVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        authorNumVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.authorNumVisibility == null || !transaction.authorNumVisibility) {
                    transaction.authorNumVisibility = true;
                    authorNumVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.authorNumVisibility = false;
                    authorNumVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.originIndVisibility == null || !transaction.originIndVisibility) {
            origIndVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            origIndVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        origIndVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.originIndVisibility == null || !transaction.originIndVisibility) {
                    transaction.originIndVisibility = true;
                    origIndVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.originIndVisibility = false;
                    origIndVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.passwordVisibility == null || !transaction.passwordVisibility) {
            passwordVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            passwordVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        passwordVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.passwordVisibility == null || !transaction.passwordVisibility) {
                    transaction.passwordVisibility = true;
                    passwordVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.passwordVisibility = false;
                    passwordVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.userdataVisibility == null || !transaction.userdataVisibility) {
            ecrDataVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            ecrDataVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        ecrDataVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.userdataVisibility == null || !transaction.userdataVisibility) {
                    transaction.userdataVisibility = true;
                    ecrDataVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.userdataVisibility = false;
                    ecrDataVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.langCodeVisibility == null || !transaction.langCodeVisibility) {
            langCodeVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            langCodeVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        langCodeVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.langCodeVisibility == null || !transaction.langCodeVisibility) {
                    transaction.langCodeVisibility = true;
                    langCodeVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.langCodeVisibility = false;
                    langCodeVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.receiptLayoutVisibility == null || !transaction.receiptLayoutVisibility) {
            receiptLayoutVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            receiptLayoutVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        receiptLayoutVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.receiptLayoutVisibility == null || !transaction.receiptLayoutVisibility) {
                    transaction.receiptLayoutVisibility = true;
                    receiptLayoutVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);

                } else {
                    transaction.receiptLayoutVisibility = false;
                    receiptLayoutVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.desCurrencyVisibility == null || !transaction.desCurrencyVisibility) {
            destCurrencyVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            destCurrencyVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        destCurrencyVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.desCurrencyVisibility == null || !transaction.desCurrencyVisibility) {
                    transaction.desCurrencyVisibility = true;
                    destCurrencyVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.desCurrencyVisibility = false;
                    destCurrencyVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.txOriginVisibility == null || !transaction.txOriginVisibility) {
            txOriginVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            txOriginVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        txOriginVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.txOriginVisibility == null || !transaction.txOriginVisibility) {
                    transaction.txOriginVisibility = true;
                    txOriginVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.txOriginVisibility = false;
                    txOriginVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        if (transaction.personalIDVisibility == null || !transaction.personalIDVisibility) {
            personalIDVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
        } else {
            personalIDVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
        }

        personalIDVisibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transaction.personalIDVisibility == null || !transaction.personalIDVisibility) {
                    transaction.personalIDVisibility = true;
                    personalIDVisibilityButton.setImageResource(R.drawable.round_check_circle_white_24dp);
                } else {
                    transaction.personalIDVisibility = false;
                    personalIDVisibilityButton.setImageResource(R.drawable.outline_disabled_visible_white_24dp);
                }
            }
        });

        saveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.name = nameEdit.getText().toString();
                transaction.amount = amountEditText.getText().toString();
                transaction.msgType = messageTypeEdit.getText().toString();
                transaction.currency = currencyEditText.getText().toString();
                transaction.sourceID = transSourceIDEdit.getText().toString();
                transaction.cardNum = cardNumEditText.getText().toString();
                transaction.cvc2 = cvc2EditText.getText().toString();
                transaction.receiptNum = receiptNumEditText.getText().toString();
                transaction.paymentReason = paymentReasonEditText.getText().toString();
                transaction.transPlace = transPlaceEditText.getText().toString();
                transaction.authorNum = authNumberEditText.getText().toString();
                transaction.originInd = originIndicatorEditText.getText().toString();
                transaction.password = passwordEditText.getText().toString();
                transaction.userdata = ecrDataEditText.getText().toString();
                transaction.langCode = languageCodeEditText.getText().toString();
                transaction.receiptLayout = receiptLayoutEditText.getText().toString();
                transaction.desCurrency = destinationCurrencyEditText.getText().toString();
                transaction.personalID = personalIDEditText.getText().toString();
                transaction.txOrigin = txOriginEditText.getText().toString();

                if(!edit)
                    Repository.getInstance(getApplicationContext()).saveTransaction(transaction);
                else
                    Repository.getInstance(getApplicationContext()).updateTransaction(transaction);

                finish();
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Repository.getInstance(getApplicationContext()).deleteTransation(String.valueOf(transaction.id));
                finish();
            }
        });


        cancelImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
