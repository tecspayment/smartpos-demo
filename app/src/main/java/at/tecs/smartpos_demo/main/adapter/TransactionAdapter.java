package at.tecs.smartpos_demo.main.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import at.tecs.smartpos_demo.R;
import at.tecs.smartpos_demo.data.repository.Repository;
import at.tecs.smartpos_demo.data.repository.entity.TransactionEntity;
import at.tecs.smartpos_demo.main.dialog.TransactionSettingsDialog;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private ArrayList<TransactionEntity> transactions;
    private final Context context;

    public TransactionAdapter(ArrayList<TransactionEntity> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageButton transSettingsButton;
        private final ImageButton transSendButton;
        private final TextView transactionName;
        private final TextView messageTypeText;
        private final TextInputLayout amountEditLayout;
        private final TextInputEditText amountEditText;
        private final TextInputEditText currencyEditText;
        private final EditText transSourceIDEditText;
        private final EditText cardNumEditText;
        private final EditText cvc2EditText;
        private final EditText receiptNumEditText;
        private final EditText paymentReasonEditText;
        private final EditText transPlaceEditText;
        private final EditText authNumberEditText;
        private final EditText originIndicatorEditText;
        private final EditText passwordEditText;
        private final EditText ecrDataEditText;
        private final EditText languageCodeEditText;
        private final EditText receiptLayoutEditText;
        private final EditText destinationCurrencyEditText;
        private final EditText txOriginEditText;
        private final EditText personalIDEditText;

        public ViewHolder(View view) {
            super(view);

            transSendButton = view.findViewById(R.id.transSendButton);
            transSettingsButton = view.findViewById(R.id.transSettingsButton);
            transactionName = view.findViewById(R.id.transactionNameLayout);
            messageTypeText = view.findViewById(R.id.messageTypeEdit);
            amountEditLayout = view.findViewById(R.id.amountEditLayout);
            amountEditText = view.findViewById(R.id.amountEditText);
            currencyEditText = view.findViewById(R.id.currencyEditText);
            transSourceIDEditText = view.findViewById(R.id.transSourceIDEdit);
            cardNumEditText = view.findViewById(R.id.cardNumEditText);
            cvc2EditText = view.findViewById(R.id.cvc2EditText);
            receiptNumEditText = view.findViewById(R.id.receiptNumEditText);
            paymentReasonEditText = view.findViewById(R.id.paymentReasonEditText);
            transPlaceEditText = view.findViewById(R.id.transPlaceEditText);
            authNumberEditText = view.findViewById(R.id.authNumberEditText);
            originIndicatorEditText = view.findViewById(R.id.originIndicatorEditText);
            passwordEditText = view.findViewById(R.id.passwordEditText);
            ecrDataEditText = view.findViewById(R.id.ecrDataEditText);
            languageCodeEditText = view.findViewById(R.id.languageCodeEditText);
            receiptLayoutEditText = view.findViewById(R.id.receiptLayoutEditText);
            destinationCurrencyEditText = view.findViewById(R.id.destinationCurrencyEditText);
            txOriginEditText = view.findViewById(R.id.txOriginEditText);
            personalIDEditText = view.findViewById(R.id.personalIDEditText);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transactions_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final TransactionEntity transaction = transactions.get(i);

        showTransaction(transaction, viewHolder);

        viewHolder.transSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTransactionSettings(transaction, context);
            }
        });

        viewHolder.transSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    private void showTransaction(TransactionEntity transaction,final ViewHolder viewHolder) {
        if(transaction.msgType != null) {
            viewHolder.messageTypeText.setText(transaction.msgType);
        }

        if(transaction.name != null) {
            viewHolder.transactionName.setText(transaction.name);
        }

        if(transaction.amount != null) {
            viewHolder.amountEditText.setText(transaction.amount);
            if(transaction.amountVisibility != null && !transaction.amountVisibility) {
                viewHolder.amountEditLayout.setVisibility(View.GONE);
            } else {
                viewHolder.amountEditLayout.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.currency != null) {
            viewHolder.currencyEditText.setText(transaction.currency);
            if(transaction.currencyVisibility != null && !transaction.currencyVisibility) {
                viewHolder.currencyEditText.setVisibility(View.GONE);
            } else {
                viewHolder.currencyEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.sourceID != null) {
            viewHolder.transSourceIDEditText.setText(transaction.sourceID);
            if(transaction.sourceIDVisibility != null && !transaction.sourceIDVisibility) {
                viewHolder.transSourceIDEditText.setVisibility(View.GONE);
            } else {
                viewHolder.transSourceIDEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.cardNum != null) {
            viewHolder.cardNumEditText.setText(transaction.cardNum);
            if(transaction.cardNumVisibility != null && !transaction.cardNumVisibility) {
                viewHolder.cardNumEditText.setVisibility(View.GONE);
            } else {
                viewHolder.cardNumEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.cvc2 != null) {
            viewHolder.cvc2EditText.setText(transaction.cvc2);
            if(transaction.cvc2Visibility != null && !transaction.cvc2Visibility) {
                viewHolder.cvc2EditText.setVisibility(View.GONE);
            } else {
                viewHolder.cvc2EditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.receiptNum != null) {
            viewHolder.receiptNumEditText.setText(transaction.receiptNum);
            if(transaction.receiptNumVisibility != null && !transaction.receiptNumVisibility) {
                viewHolder.receiptNumEditText.setVisibility(View.GONE);
            } else {
                viewHolder.receiptNumEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.paymentReason != null) {
            viewHolder.paymentReasonEditText.setText(transaction.paymentReason);
            if(transaction.paymentReasonVisibility != null && !transaction.paymentReasonVisibility) {
                viewHolder.paymentReasonEditText.setVisibility(View.GONE);
            } else {
                viewHolder.paymentReasonEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.transPlace != null) {
            viewHolder.transPlaceEditText.setText(transaction.transPlace);
            if(transaction.transPlaceVisibility != null && !transaction.transPlaceVisibility) {
                viewHolder.transPlaceEditText.setVisibility(View.GONE);
            } else {
                viewHolder.transPlaceEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.authorNum != null) {
            viewHolder.authNumberEditText.setText(transaction.transPlace);
            if(transaction.authorNumVisibility != null && !transaction.authorNumVisibility) {
                viewHolder.authNumberEditText.setVisibility(View.GONE);
            } else {
                viewHolder.authNumberEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.originInd != null) {
            viewHolder.originIndicatorEditText.setText(transaction.originInd);
            if(transaction.originIndVisibility != null && !transaction.originIndVisibility) {
                viewHolder.originIndicatorEditText.setVisibility(View.GONE);
            } else {
                viewHolder.originIndicatorEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.password != null) {
            viewHolder.passwordEditText.setText(transaction.password);
            if(transaction.passwordVisibility != null && !transaction.passwordVisibility) {
                viewHolder.passwordEditText.setVisibility(View.GONE);
            } else {
                viewHolder.passwordEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.userdata != null) {
            viewHolder.ecrDataEditText.setText(transaction.userdata);
            if(transaction.userdataVisibility != null && !transaction.userdataVisibility) {
                viewHolder.ecrDataEditText.setVisibility(View.GONE);
            } else {
                viewHolder.ecrDataEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.langCode != null) {
            viewHolder.languageCodeEditText.setText(transaction.langCode);
            if(transaction.langCodeVisibility != null && !transaction.langCodeVisibility) {
                viewHolder.languageCodeEditText.setVisibility(View.GONE);
            } else {
                viewHolder.languageCodeEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.receiptLayout != null) {
            viewHolder.receiptLayoutEditText.setText(transaction.receiptLayout);
            if(transaction.receiptLayoutVisibility != null && !transaction.receiptLayoutVisibility) {
                viewHolder.receiptLayoutEditText.setVisibility(View.GONE);
            } else {
                viewHolder.receiptLayoutEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.desCurrency != null) {
            viewHolder.destinationCurrencyEditText.setText(transaction.desCurrency);
            if(transaction.desCurrencyVisibility != null && !transaction.desCurrencyVisibility) {
                viewHolder.destinationCurrencyEditText.setVisibility(View.GONE);
            } else {
                viewHolder.destinationCurrencyEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.txOrigin != null) {
            viewHolder.txOriginEditText.setText(transaction.txOrigin);
            if(transaction.txOriginVisibility != null && !transaction.txOriginVisibility) {
                viewHolder.txOriginEditText.setVisibility(View.GONE);
            } else {
                viewHolder.txOriginEditText.setVisibility(View.VISIBLE);
            }
        }

        if(transaction.personalID != null) {
            viewHolder.personalIDEditText.setText(transaction.personalID);
            if(transaction.personalIDVisibility != null && !transaction.personalIDVisibility) {
                viewHolder.personalIDEditText.setVisibility(View.GONE);
            } else {
                viewHolder.personalIDEditText.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showTransactionSettings(TransactionEntity transactionEntity, final Context context) {
        TransactionSettingsDialog dialog = new TransactionSettingsDialog(context, R.style.CustomDialogTheme);
        dialog.setTransaction(transactionEntity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                transactions = Repository.getInstance(context).getAllTransactions();

                notifyDataSetChanged();
            }
        });
        dialog.show();
    }
}
